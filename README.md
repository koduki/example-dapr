# dapr-app project

This is a sample project for Dapr.

https://github.com/dapr/samples/tree/master/1.hello-world


## Running the application in dev mode with dapr

You can run your application in dev mode that enables live coding using:

```bash
dapr run --app-id javaapp --app-port 8080 --port 3500 ./mvnw quarkus:dev 
```

You can test about state perstance by following command:

```bash
dapr invoke --app-id javaapp --method neworder --payload '{"data": { "orderId": "1" } }' 
curl http://localhost:3500/v1.0/invoke/javaapp/method/order
```

