package dev.nklab.example.dapr;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HelloResource {

    @ConfigProperty(name = "daprapp.daprport")
    String daprPort;

    String stateStoreName = "statestore";

    @GET
    @Path("/order")
    public Map<String, Object> order() throws IOException, InterruptedException {
        return Map.of("orderId", get(stateUrl() + "/order").body());
    }

    @POST
    @Path("/neworder")
    public HttpResponse neworder(Map<String, Map<String, Object>> data) throws IOException, InterruptedException {
        System.out.println("orderId: " + data.get("data").get("orderId"));

        var items = List.of(Map.of("key", "order", "value", data.get("data").get("orderId")));
        return post(stateUrl(), items);
    }

    private String stateUrl() {
        return "http://localhost:" + daprPort + "/v1.0/state/" + stateStoreName;
    }

    private HttpResponse<String> post(String url, List<Map<String, Object>> items) throws IOException, InterruptedException, JsonProcessingException {
        var mapper = new ObjectMapper();
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(items)))
                .setHeader("Content-Type", "application/json")
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> get(String url) throws InterruptedException, IOException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .setHeader("Content-Type", "application/json")
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
