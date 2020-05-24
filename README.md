# dapr-app project

This is a sample project for Dapr.

https://github.com/dapr/samples/tree/master/1.hello-world


## Running the API

You can run your application in dev mode that enables live coding using:

```bash
cd api-with-java 
dapr run --app-id javaapp --app-port 8080 --port 3500 ./mvnw quarkus:dev 
```

You can test about state perstance by following command:

```bash
dapr invoke --app-id javaapp --method neworder --payload '{"data": { "orderId": "1" } }' 
curl http://localhost:3500/v1.0/invoke/javaapp/method/order
```

You can test about state perstance by following command:

```bash
dapr invoke --app-id javaapp --method neworder --payload '{"data": { "orderId": "1" } }' 
curl http://localhost:3500/v1.0/invoke/javaapp/method/order
```

## Running the Client 

You can run your application in dev mode that enables live coding using:

```bash
cd client-with-ruby
dapr run --app-id rubyapp --port 3600 ruby app.rb 3600
```

