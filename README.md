# Microservices with Spring Boot, Axon CQRS/ES & Docker

This application demonstrates how to build Microservices using the CQRS/ES pattern with Java, Spring Boot, Axon and Docker. This repository contains all the sample code you'll need to implement all of the following features:-

1. Scalable Microservices using Java and [Spring Boot](http://projects.spring.io/spring-boot/)
2. Command and Query Responsibility Separation (CQRS) using [Axon Framework v2](http://www.axonframework.org/)
3. Event Sourcing (ES) and Event Driven Architecture using Axon, [MongoDB](https://www.mongodb.com/) and [RabbitMQ](https://www.rabbitmq.com/)
4. Build, Ship and Run with Containers using [Docker](http://docker.com)
5. Cloud features like centralised configuration and registration using [String Cloud](http://cloud.spring.io)
6. API Description using [Swagger](http://swagger.io)

## How it works

The demonstration revolves around a fictitious `Product` master data application similar to that which you would find in most retail or manufacturing companies. The business domain model demonstrated here is a simple one - products can be added, stored, searched and retrieved using a simple RESTful API.

The application is built using the [CQRS architectural pattern](http://martinfowler.com/bliki/CQRS.html). In a CQRS application, commands like `add` are physically separated from queries like `view (where id=1)`. In this example the domain's codebase is quite literally split two components - a **command-side microservice** and a **query-side microservice**. These microservices have a single responsibility; feature their own datastores; and can be deployed and scaled independently of each other.  This is CQRS and microservices in their most literal. Neither CQRS or microservices _have_ to be implemented in this way, but for the purpose of this demonstration I've chosen to create a very clear separation of the read and write concerns.

The logical architecture looks like this:-

![CQRS Architecture](https://github.com/benwilcock/microservice-sampler/blob/master/slides/CQRS-Architecture-01.png "CQRS Architecture")

Both the command-side and the query-side microservices have been developed using the Spring Boot framework for Java. All communication between the command and query microservices is purely `event-driven`. The events are passed between the microservice components using RabbitMQ messaging. Messaging provides a scalable means of passing events between processes, microservices, legacy systems and other parties in a loosely coupled fashion. 

> Notice how none of the services shares it's database with another. This is important because of the high degree of autonomy it affords each service, which in turn helps the individual services to scale independently of the others in the system.

> For more on CQRS architecture, check out my [Slideshare on CQRS Microservices](http://www.slideshare.net/BenWilcock1/microservice-architecture-with-cqrs-and-event-sourcing).

## More about the Command-side Microservice

Commands are _actions which change state_. The command-side microservice contains all the domain's logic and business rules. Commands are used to add new Products, or to change their state. The execution of these commands on a particular Product results in `Events` being generated which are persisted by Axon and propagated out to other processes (as many processes as you like) via RabbitMQ messaging. In event-sourcing, events are the sole record of state for the system. They are used by the system to describe and re-build the current state of an entity on demand, by replaying it's events one at a time until all previous events have been re-applied. This sounds slow, but actually it's really fast and can be tuned further using 'snapshots'. 

> In Domain Driven Design (DDD) the 'Product' entity is often referred to as an `Aggregate` or an `AggregateRoot`.

## More about the Query-side Microservice

The query-side microservice acts as an event-listener and view. It listens for the `Events` being emitted by the command-side and processes them into whatever view or composite makes most sense. In this particular example, the query-side simply builds and maintains a *materialised view*. This view tracks the state of the individual Products (in terms of what they are and whether they are saleable or un-saleable). The query-side can be replicated many times for scalability and the messages held by the RabbitMQ queues can be made to be durable, so they can temporarily store messages on behalf of the query-side if it goes down.

The command-side and the query-side both have REST API's which can be used to access their capabilities.

> These REST API's are a 'work in progress' and are subject to change.

Read the [Axon documentation](http://www.axonframework.org) for the finer details of how Axon brings CQRS and Event Sourcing to your apps, as well as lots of detail on how it's configured and used (using Spring for the setup and some Java extensions and annotations for the code).

## Running the Demo

Running the demo is easy but you'll need to have the following software installed on your machine first...

- [Docker](https://www.docker.com/) (I'm using v1.8.2)
- [Docker-compose](https://www.docker.com/) (I'm using v1.7.1)

If you have all the required software, you can run the demo by following the process outlined below. I'm using [Ubuntu 16.04](http://ubuntu.com) as my development OS. 

> If you're developing on Windows or Mac then Docker is a bit more fiddly unless you try out the native '[Docker for Mac](https://blog.docker.com/2016/03/docker-for-mac-windows-beta/)' or '[Docker for Windows](https://blog.docker.com/2016/03/docker-for-mac-windows-beta/)'. Both in BETA at the time of writing and neither have been tested for compatibility with this demo.

### Step 1: Get the Docker-compose config file

In a new empty folder, at the terminal execute the following command to download the latest docker-compose configuration file (YAML). 

```bash
$ wget https://raw.githubusercontent.com/benwilcock/microservice-sampler/master/docker-compose.yml
```

> Don't change the file name - Docker defaults to looking for a file named 'docker-compose.yml'

### Step 2: Start the Microservices

Because we're using docker-compose, starting the microservices is simply a case of executing the following command window from within the folder you created for step 1... 

```bash
$ docker-compose up
```

You'll see lots of logging output in the terminal as the servers images are downloaded and then proceed to boot. There are six servers in total, they are 'mongodb', 'rabbitmq', 'config', 'discovery', 'product-cmd-side', and 'product-qry-side'. If you want to see which docker instances are running on your machine at any time, open a separate terminal and execute the following command:-
 
```bash
$ docker ps
```

Once the servers are all up and running (this can take some time at first) you can have a look around using your browser. You should be able to access:-
 
 1. [The Rabbit Management Console](http://localhost:15672) on port 15672
 2. [The Eureka Discovery Server Console](http://localhost:8761) on port 8761
 3. [The Configuration Server](http://localhost:8888/mappings) on port 8888
 4. [The Product Command Side Swagger API Docs](http://localhost:9000/swagger-ui.html) on port `9000`
 5. [An empty Product repository on the query-side](http://localhost:9001/products) (responses are in JSON format) on port `9001`

### Step 3: Adding and Viewing Products.

So far so good. Now we want to test the addition of products. 

In this _hands-on manual test_ we'll issue an `add` command to the command-side REST API which is listening on port 9000. When the command-side has processed the command a 'ProductAdded' event is stored onto MongoDB and forwarded to the query-side via the RabbitMQ messaging server. The query-side then processes this event and adds a record for the product to it's materialised-view (actually a H2 in memory database for this simple demo). Finally we'll use the query-side microservice on port `9090` to lookup information regarding the product we added. As you do these tasks, you should observe some logging output in the terminal window. 

#### Step 3.1: Add A New Product

To perform this manual test this we need to first **open a second terminal window** from where we can issue some CURL commands without having to stop the docker instances we have running in the first window.

For the purposes of this test, we'll add an MP3 product to our product catalogue with the name 'Everything is Awesome'. To do this we can use the command-side REST API on port `9000` and issue it with a POST request as follows.

```bash
$ curl -X POST -v --header "Content-Type: application/json" --header "Accept: */*" "http://localhost:9000/products/add/1?name=Everything%20Is%20Awesome"
```

You should see the following response. If the response code is `HTTP/1.1 201 Created` then the product "Everything is Awesome" has been added to the command-side event-sourced repository successfully.

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

Now lets check that regular users can also view the product that we just added. To do this we use the query-side API on port `9001` and issue a simple 'GET' request.

```bash
$ curl http://localhost:9090/products/1
```

You should see the following output. This shows that the query-side microservice has a record for our newly added MP3 product. The product is listed as non-saleable (saleable = false).

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

That's it. Go ahead and add some more products if you like but be careful not to try to re-use the same product ID or you'll see an error.

## Other highlights

No Microservice demonstrator would be complete without a service registry, so I've added a Eureka service using Spring Cloud Netflix which is available on port `8761`. The command-side and query-side microservices both register themselves during startup and can be seen on the Eureka console. Registering can be useful for resilience and allows for client side load balancing.

