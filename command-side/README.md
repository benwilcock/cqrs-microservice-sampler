# The command-side API

The Product application's command-side has a REST API which you can use to send commands to the application. The full command-side API has a helpful Swagger 2 definition which you can access with your web browser once the application has booted.

## Booting the command-side

To boot the command side application (having made sure MongoDB and RabbitMQ are both available) issue the following command at the prompt...

```bash
$ ./gradlew command-side:bootRun
```

## Viewing the API Documentation

You can view the latest documentation for the command-side API by pointing your browser at [http://localhost:9000/swagger-ui.html](http://localhost:9000/swagger-ui.html). this will present you with a friendly form which you can use to send requests and view responses.

## Quickstart

If you just want to try it out, use a simple Curl command like this one.

```bash
curl -X POST -v --data "name=Everything is Awesome" http://localhost:9000/api/products/add/{Insert your GUID here}
```