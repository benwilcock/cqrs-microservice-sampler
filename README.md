# microservice-sampler

This application demonstrates the use of the CQRS pattern using Java (Spring Boot) Microservices. This code illustrates the use of:-

1. Microservices using Java and [Spring Boot](http://projects.spring.io/spring-boot/)
2. Command and Query Responsibility Separation (CQRS) using [Axon Framework](http://www.axonframework.org/)
3. Event Sourcing and Event Driven Architecture using Axon, [MongoDB](https://www.mongodb.com/) and [RabbitMQ](https://www.rabbitmq.com/)
4. Build, Ship and Run with Containers using [Docker](http://docker.com)

## How it works

The demonstration uses a fictitious `Product` master data application similar to that which you would find in most retail or manufacturing organisations. The model demonstrated here is very basic. Products can be added, stored, searched and retrieved by users using a simple REST API.

In a CQRS application, commands like 'add' are physically separated from queries like view where id=1. In this case the Product domain's implementation code is quite literally split into a *command-side* microservice and a *query-side* microservice.  This is CQRS in its most literal form. CQRS doesn't have to be implemented this way if you don't want it to be, but for the purpose of this demonstration there is a clear and intentional split.

Both microservices use Spring Boot. Communication between the two microservices is `event-driven`. Events are passed between the microservice components using RabbitMQ messaging. This provides a scalable means of passing events between processes, microservices, legacy systems and other parties in a loosely coupled fashion.

## More about the 'Command-side'

Commands are _actions which change state_ in some way. The command-side microservice processes all commands. In this demonstration commands are used to add new Product, or to make them 'salaeable' or 'un-saleable'. The execution of these commands on a particular Product results in `Events` being generated which are persisted by Axon and propagated out to other VM's (as many VM's as you like) via RabbitMQ messaging. In event-sourcing, events are the sole records in the system. They are used by the system to describe and re-build aggregates on demand, one event at a time. 

> In DDD the 'Product' entity is often referred to as an `Aggregate` or `AggregateRoot`.

## More about the 'Query-side'

The query-side microservice acts as an event-listener and processor. It listens for the `Events` and processes them in whatever way makes the most sense. In this particular demonstration, the query-side just builds and maintains a *materialised view* which tracks the state of the individual Products (in terms of whether they are saleable or un-saleable). The query-side can be replicated many times for scalability and the messages held by the RabbitMQ queues are durable, so they can be temporarily stored on behalf of the event-listener if it goes down.

The command-side and the query-side both have REST API's which can be used to access their capabilities.

> These REST API's are a 'work in progress'.

Read the [Axon documentation](http://www.axonframework.org/download/) for the finer details of how Axon generally operates to bring you CQRS and Event Sourcing to your apps, as well as lots of detail on how it all get's configured (spoiler: it's mostly spring-context XML for the setup and some Java extensions and annotations within the code).

## Running the Demo yourself...

To run the demo you'll need to have the following application installed on your machine...

- [Linux](http://ubuntu.com) (I'm using Ubuntu 16.04)
- [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (I'm using v1.8.0_60)
- [Git](https://git-scm.com/) (I'm using v1.9.1)
- [Docker](https://www.docker.com/) (I'm using v1.8.2)
- [Docker-compose](https://www.docker.com/) (I'm using v1.7.1)

If you have installed all the required software, you can run the demo by following the process outlined below. If your using Windows or Mac, Docker is a bit more fiddly to use unless you use the native '[Docker for Mac](https://blog.docker.com/2016/03/docker-for-mac-windows-beta/)' or '[Docker for Windows](https://blog.docker.com/2016/03/docker-for-mac-windows-beta/)' which is in BETA at the time of writing.

### Step 1: Build the Microservice docker images

First you will need to build the code and add the generated docker images to your local docker repository. Navigate to the folder where you cloned this git repository and execute the following command at the command line (syntax assumes linux). Gradlew is provided for your convenience - you shouldn't need gradle installed to follow along.

```bash
$ ./gradlew clean test image
```

If you now check your docker images you should see the following images in your list of images.

```bash
$ docker images
REPOSITORY                        TAG                 IMAGE ID            CREATED             SIZE
benwilcock/product-query-side     latest              85324407071a        0 minutes ago      270.3 MB
benwilcock/discovery-service      latest              ae715b59fd40        0 minutes ago      241.7 MB
benwilcock/product-command-side   latest              67780f537e8c        0 minutes ago      248.5 MB
```

### Step 2: Run the server group

Because we're using docker-compose, this should simply be a case of executing the following command at the command line... 

```bash
$ docker-compose up
```

You'll see lots of logging output as the servers spin up. There are 5 servers in total, they are 'mongodb', 'rabbitmq', 'discovery', 'product-cmd-side', and 'product-qry-side'. If you want to see what docker instances are running at any time, open a separate terminal and execute the following command:-
 
```bash
$ docker ps
CONTAINER ID        IMAGE                                    COMMAND                  CREATED             STATUS              PORTS                                                                                         NAMES
fffc443838ca        benwilcock/product-command-side:latest   "java -Djava.security"   38 minutes ago      Up 38 minutes       0.0.0.0:9000-9001->9000-9001/tcp                                                              product-cmd-side
6e156d5fab6d        benwilcock/product-query-side:latest     "java -Djava.security"   38 minutes ago      Up 38 minutes       0.0.0.0:9090-9091->9090-9091/tcp                                                              product-qry-side
dac5e70f6750        benwilcock/discovery-service             "java -Djava.security"   38 minutes ago      Up 38 minutes       0.0.0.0:8761->8761/tcp                                                                        discovery
74b6a0ceb94b        mongo:2.4                                "/entrypoint.sh mongo"   38 minutes ago      Up 38 minutes       0.0.0.0:32771->27017/tcp                                                                      mongodb
6114c16b53f6        rabbitmq:3-management                    "/docker-entrypoint.s"   38 minutes ago      Up 38 minutes       4369/tcp, 5671/tcp, 15671/tcp, 25672/tcp, 0.0.0.0:15672->15672/tcp, 0.0.0.0:32770->5672/tcp   rabbitmq
```

Once the servers are up you can have a poke around using a browser if you like. Exposed are:-
 
 1. [The Rabbit Management Console](http://localhost:15672) on port 15672
 2. [The Eureka Discovery Server Console](http://localhost:8761) on port 8761
 3. [The Product Command Side Swagger API Docs](http://localhost:9000/swagger-ui.html) on port 9000
 4. [An empty Product repository on the query-side](http://localhost:9090/products) (JSON format) on port 9090

### Step 3: Test the application.

So far so good. Now we want to test the addition of products. This will test that 

- products are created and persisted in the command-side microservice; 
- that events are propagated between processes and sent via rabbitmq to the query-side microservice; 
- and the querying of data in the query-side's materialised view. 

In this _hands-on manual test_ we'll issue an 'Add' command to the command-side REST API which is listening on port 9000. When the command-side has processed the command a 'ProductAdded' event is generated and sent to the query-side via the RabbitMQ service. The query-side then processes this event and adds a record for the product to it's materialised-view (actually a H2 in memory database for this simple demo). As you do this, you should observe some logging output in the terminal window while the test is in progress.

To run this test this we need to open a second terminal window from which we can issue some CURL commands without having to stop docker. 

#### Step 3.1: Add A New Product

Let's add an MP3 product to our product catalogue with the name 'Everything is Awesome'.

```bash
$ curl -X POST --header "Content-Type: application/json" --header "Accept: */*" "http://localhost:9000/products/add/1?name=Everything%20Is%20Awesome"
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

If the response code is `HTTP/1.1 201 Created` then the product "Everything is Awesome" will have been added to the command-side successfully.
 
#### Step 3.2: Query for the Product

Now lets check that users can see the product that we just added. To do this we use the query-side API and issue a simple 'GET' request.

```bash
$ curl http://localhost:9090/products/1
```

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

This JSON output shows that the query-side microservice (operating on port 9090 you'll notice) has a record for our newly added product. The product is listed as not saleable (saleable = false). As HATEOAS is switched on in Spring Boot REST, you have also been offered some other links which you can also traverse with curl.

