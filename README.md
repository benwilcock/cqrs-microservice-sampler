#Microservices with Docker, Spring Boot, Spring Cloud and Axon

This is a non-trivial demonstration of how to build a CQRS microservice application consisting of several collaborating microservices. It combines together all of the following elements in order to produce one logical application - a simple Product Master Data service.

 - Service gateway and registry using [Spring Cloud Netflix](https://cloud.spring.io/spring-cloud-netflix/) (Zuul, Eureka)
 - External configuration using [Spring Cloud Config](https://cloud.spring.io/spring-cloud-config/)
 - Java Microservices with [Spring Boot](http://projects.spring.io/spring-boot/)
 - Command & Query responsibility Separation with the [Axon CQRS Framework](http://www.axonframework.org/)
 - Event Sourcing & Materialised Views with RabbitMQ, MongoDB and H2

#About the Author

[Ben Wilcock](https://uk.linkedin.com/in/benwilcock) works for Pivotal as a Cloud Solutions Architect. Ben has a passion for microservices, cloud and mobile applications and helps [Pivotal's Cloud Foundry](http://pivotal.io/platform) customers to become more responsive, innovate faster and gain greater returns from their software investments. Ben is also a respected technology [blogger](http://benwilcock.wordpress.com) who's articles have featured in [DZone](https://dzone.com/users/296242/benwilcock.html), [Java Code Geeks](https://www.javacodegeeks.com/author/ben-wilcock/), [InfoQ](https://www.infoq.com/author/Ben-Wilcock) and more.

##Running the Demo

Running the demo is easy. The whole environment has been packaged to be run as a series of Docker containers. To run the code, you'll need to have the following software installed on your machine. For reference I'm using Ubuntu 16.04 as my OS, but I have also tested the app on the new Docker for Windows Beta successfully.

 - Java SDK 8
 - [Docker](https://www.docker.com) (I'm using v1.8.2)
 - Docker-compose (I'm using v1.7.1)

If you have these, you can run the demo by following the process outlined below.

>If you have either MongoDB or RabbitMQ running locally already, please shut down these services before continuing in order to avoid port clashes.

###Step 1: Build the containers

In a new empty folder, at the terminal execute the following command to download the latest code for this demo.

```bash
$ git clone https://github.com/benwilcock/microservice-sampler.git
```

Then build the docker container images.

```bash
$ cd microservice-sampler
$ ./gradlew clean image
```

This will create a series of Docker container images, one for each of the spring-boot microservices used in this demo.

###Step 2: Start the Microservices

There are seven Docker container images in this microservice group, they are 'mongodb', 'rabbitmq', 'config', 'discovery', `gateway-service', 'product-cmd-side', and 'product-qry-side'. The logical architecture looks like this:-

![Architecture](https://github.com/benwilcock/microservice-sampler/blob/master/slides/CQRS-Architecture-02.png "Architecture")

Because we're using docker-compose, starting the microservices is now simply a case of executing the following command.

```bash
$ docker-compose -f wip.yml up
```

If you want to see which docker instances are running on your machine at any time, open a separate terminal and execute the following command:-

```bash
$ docker ps
```

###Step 3: Integration Test (Manual)

So far so good. Now we want to test the addition of products. In a new terminal window (ctrl-alt-t in Ubuntu), execute the following curl request...

```bash
$ curl -X POST -v --header "Content-Type: application/json" --header "Accept: */*" "http://localhost:8080/commands/products/add/1?name=Everything%20Is%20Awesome"
```

>If you're using the public beta of Docker for Mac or Windows (which is highly recommended), you may need to swap 'localhost' for the IP address shown when you ran 'docker ps' to observe the running servers.

You should see the following response.

```bash
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /commands/products/add/1?name=Everything%20Is%20Awesome HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.47.0
> Content-Type: application/json
> Accept: */*
>
< HTTP/1.1 201 Created
< Date: Wed, 29 Jun 2016 14:14:26 GMT
< X-Application-Context: gateway-service:production:8080
< Date: Wed, 29 Jun 2016 14:14:26 GMT
< Transfer-Encoding: chunked
< Server: Jetty(9.2.16.v20160414)
```

The response code should be `HTTP/1.1 201 Created`. This means that the MP3 product "Everything is Awesome" has been added to the command-side event-sourced repository successfully.

Now lets check that we can view the product that we just added. To do this we use the query-side API and issue a simple 'GET' request.

```bash
$ curl http://localhost:8080/queries/products/1
```

You should see the following output. This shows that the query-side microservice has a record for our newly added MP3 product. The product is listed as non-saleable (saleable = false).

```json
{
  "name" : "Everything Is Awesome",
  "saleable" : false,
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/queries/products/1"
    },
    "product" : {
      "href" : "http://localhost:8080/queries/products/1"
    }
  }
}
```

That's it! Go ahead and repeat the test to add some more products if you like, just be careful not to reuse the same product ID when you POST or you'll get a `409 Conflict` error.

If you're familiar with MongoDB you can inspect the database to see all the events that you've created. Similarly if you know your way around the RabbitMQ Management Console you can see the messages as they flow between the command-side and query-side microservices. If you like you can also execute the integration tests using the command..

```bash
$ ./gradlew integration-test:integrationTest
```
