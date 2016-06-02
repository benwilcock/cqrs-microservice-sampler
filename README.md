# Microservices with Spring Boot, Axon CQRS/ES & Docker

This application demonstrates how to build Microservices using the CQRS/ES pattern with Java, Spring Boot, Axon and Docker. By following this code sample you'll see how to implement:-

1. Scalable Microservices using Java and [Spring Boot](http://projects.spring.io/spring-boot/)
2. Command and Query Responsibility Separation (CQRS) using [Axon Framework v2](http://www.axonframework.org/)
3. Event Sourcing (ES) and Event Driven Architecture using Axon, [MongoDB](https://www.mongodb.com/) and [RabbitMQ](https://www.rabbitmq.com/)
4. Build, Ship and Run with Containers using [Docker](http://docker.com)

## How it works

The demonstration uses a fictitious `Product` master data application similar to that which you would find in most retail or manufacturing organisations. The business domain model demonstrated here is very basic. Products can be added, stored, searched and retrieved using a simple REST API.

In a CQRS application, commands like `add` are physically separated from queries like `view (where id=1)`. In this case the domain's codebase is quite literally split two pieces - a **command-side microservice** and a **query-side microservice** that can  be deployed and scaled independently of each other.  This is CQRS in its most literal form. CQRS doesn't have to be implemented this way if you don't want it to be, but for the purpose of this demonstration there is a clear and intentional split.

Both the command-side and the query-side microservices use Spring Boot. Communication between the two microservices is `event-driven`. Events are passed between the two microservice components using RabbitMQ messaging. This provides a scalable means of passing events between processes, microservices, legacy systems and other parties in a loosely coupled fashion.

## More about the Command-side Microservice

Commands are _actions which change state_ in some way. The command-side microservice processes all commands. In this demonstration commands are used to add new Product, or to make them 'salaeable' or 'un-saleable'. The execution of these commands on a particular Product results in `Events` being generated which are persisted by Axon and propagated out to other VM's (as many VM's as you like) via RabbitMQ messaging. In event-sourcing, events are the sole records in the system. They are used by the system to describe and re-build aggregates on demand, one event at a time. 

> In DDD the 'Product' entity is often referred to as an `Aggregate` or an `AggregateRoot`.

## More about the Query-side Microservice

The query-side microservice acts as an event-listener and processor. It listens for the `Events` and processes them in whatever way makes the most sense. In this particular demonstration, the query-side just builds and maintains a *materialised view* which tracks the state of the individual Products (in terms of whether they are saleable or un-saleable). The query-side can be replicated many times for scalability and the messages held by the RabbitMQ queues are durable, so they can be temporarily stored on behalf of the event-listener if it goes down.

The command-side and the query-side both have REST API's which can be used to access their capabilities.

> These REST API's are a 'work in progress' and are subject to change.

Read the [Axon documentation](http://www.axonframework.org) for the finer details of how Axon brings CQRS and Event Sourcing to your apps, as well as lots of detail on how it's used (Spring for the setup and some Java extensions and annotations for the code).

## Running the Demo

To run the demo you'll need to have the following software installed on your machine...

- [Linux](http://ubuntu.com) (I'm using Ubuntu 16.04)
- [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (I'm using v1.8.0_60)
- [Git](https://git-scm.com/) (I'm using v1.9.1)
- [Docker](https://www.docker.com/) (I'm using v1.8.2)
- [Docker-compose](https://www.docker.com/) (I'm using v1.7.1)

If you have all the required software, you can run the demo by following the process outlined below. If you're using Windows or Mac, Docker is a bit more fiddly to use unless you use the native '[Docker for Mac](https://blog.docker.com/2016/03/docker-for-mac-windows-beta/)' or '[Docker for Windows](https://blog.docker.com/2016/03/docker-for-mac-windows-beta/)' which are both in BETA at the time of writing and haven't been tested.

### Step 1: Build the Docker images

First you will need to build the code and have the Docker images it generates added to your local Docker image repository. To do this simply navigate to the folder where you cloned this git repository and execute the following command at the command line (syntax assumes linux). The `gradlew` script has been provided for your convenience - you don't need to install gradle in order to follow along.

```bash
$ ./gradlew clean test image
```

If you now check your Docker images repository you should see the following images in your list.

```bash
$ docker images
REPOSITORY                        TAG                 IMAGE ID            CREATED             SIZE
benwilcock/product-query-side     latest              85324407071a        0 minutes ago      270.3 MB
benwilcock/discovery-service      latest              ae715b59fd40        0 minutes ago      241.7 MB
benwilcock/product-command-side   latest              67780f537e8c        0 minutes ago      248.5 MB
```

### Step 2: Spin up the Servers

Because we're using docker-compose, starting the microservices is simply a case of executing the following command in a Terminal window... 

```bash
$ docker-compose up
```

You'll see lots of logging output in the terminal as the servers spin up. There are five servers in total, they are 'mongodb', 'rabbitmq', 'discovery', 'product-cmd-side', and 'product-qry-side'. If you want to see which docker instances are running on your machine at any time, open a separate terminal and execute the following command:-
 
```bash
$ docker ps
CONTAINER ID        IMAGE                                    COMMAND                  CREATED             STATUS              PORTS                                                                                         NAMES
fffc443838ca        benwilcock/product-command-side:latest   "java -Djava.security"   38 minutes ago      Up 38 minutes       0.0.0.0:9000-9001->9000-9001/tcp                                                              product-cmd-side
6e156d5fab6d        benwilcock/product-query-side:latest     "java -Djava.security"   38 minutes ago      Up 38 minutes       0.0.0.0:9090-9091->9090-9091/tcp                                                              product-qry-side
dac5e70f6750        benwilcock/discovery-service             "java -Djava.security"   38 minutes ago      Up 38 minutes       0.0.0.0:8761->8761/tcp                                                                        discovery
74b6a0ceb94b        mongo:2.4                                "/entrypoint.sh mongo"   38 minutes ago      Up 38 minutes       0.0.0.0:32771->27017/tcp                                                                      mongodb
6114c16b53f6        rabbitmq:3-management                    "/docker-entrypoint.s"   38 minutes ago      Up 38 minutes       4369/tcp, 5671/tcp, 15671/tcp, 25672/tcp, 0.0.0.0:15672->15672/tcp, 0.0.0.0:32770->5672/tcp   rabbitmq
```

Once the servers are all up and running (this can take some time depending on your system) you can have a look around using your browser. You should be able to access:-
 
 1. [The Rabbit Management Console](http://localhost:15672) on port 15672
 2. [The Eureka Discovery Server Console](http://localhost:8761) on port 8761
 3. [The Product Command Side Swagger API Docs](http://localhost:9000/swagger-ui.html) on port `9000`
 4. [An empty Product repository on the query-side](http://localhost:9090/products) (responses are in JSON format) on port `9090`

### Step 3: Adding and Viewing Products.

So far so good. Now we want to test the addition of products. 

In this _hands-on manual test_ we'll issue an `add` command to the command-side REST API which is listening on port 9000. When the command-side has processed the command a 'ProductAdded' event is stored onto MongoDB and forwarded to the query-side via the RabbitMQ messaging server. The query-side then processes this event and adds a record for the product to it's materialised-view (actually a H2 in memory database for this simple demo). Finally we'll use the query-side microservice on port `9090` to lookup information regarding the product we added. As you do these tasks, you should observe some logging output in the terminal window.

To run this test this we need to open a second terminal window from which we can issue some CURL commands without having to stop the docker instances we have running. 

#### Step 3.1: Add A New Product

Let's add an MP3 product to our product catalogue with the name 'Everything is Awesome'. To do this we use the command-side API on port `9000` and issue a POST.

```bash
$ curl -X POST -v --header "Content-Type: application/json" --header "Accept: */*" "http://localhost:9000/products/add/1?name=Everything%20Is%20Awesome"
```

You should see the following output and if the response code is `HTTP/1.1 201 Created` then the product "Everything is Awesome" has been added to the command-side successfully.

```bash
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 9000 (#0)
> POST /products/add/1?name=Everything%20Is%20Awesome HTTP/1.1
> Host: localhost:9000
> User-Agent: curl/7.47.0
> Content-Type: application/json```bash
> Accept: */*$ http://localhost:9090/products/1
< HTTP/1.1 201 Created
< Date: Thu, 02 Jun 2016 13:37:07 GMTThis 
< X-Application-Context: product-command-side:9000
< Content-Length: 0
< Server: Jetty(9.2.16.v20160414)
```
 
#### Step 3.2: Query for the Product

Now lets check that users can see the product that we just added. To do this we use the query-side API on port `9090` and issue a simple 'GET' request.

```bash
$ curl http://localhost:9090/products/1
```

You should see the following output. This shows that the query-side microservice (operating on port 9090 you'll notice) has a record for our newly added product. The product is listed as not saleable (saleable = false).

```json
{
    name: "Everything Is Awesome",
    saleable: false,
    _links: {
        self: {
            href: "http://localhost:9090/products/1"
        },
        product: {
            href: "http://localhost:9090/products/1"
        }
    }
}
```

## Other highlights

No Microservice demonstrator would be complete without a service registry, so I've added a Eureka service using Spring Cloud Netflix which is available on port `8761`. The command-side and query-side microservices both register themselves during startup and can be seen on the Eureka console.

