# microservice-sampler

This is a demonstration of the mocroservices pattern in `Java`. It's built to illustrate the use of a number of techniques and patterns in one simple 'Todo' application...

1. Microservices 
2. Command and Query Responsibility Separation (CQRS)
3. Event Sourcing

It leverages the following technologies to do this...

- [Spring Boot](http://projects.spring.io/spring-boot/) (v1.2.6)
- [Axon Framework](http://www.axonframework.org/) (v2.4)
- [RabbitMQ](https://www.rabbitmq.com/) (v3.5.4) Axon supports any Spring AMQP supported platform.
- [MongoDB](https://www.mongodb.com/) (v.latest) Axon also supports JDBC & JPA based event-stores.

## How it works

The domain is literally split into a Command-side microservice and a Query-side microservice (this is CQRS in its most literal form, although it doesn't have to be this way if you don't want it to be).

The **command-side** can process commands such as creating and completing or un-completing a Todo item. The execution of these commands results in `Events` which are persisted by Axon (using MongoDB) and propagated out to other VM's (as many as you like) via RabbitMQ.

The **query-side** listens for `Events` coming from RabbitMQ and process them in whatever way makes the most sense. In this simple Todo demo, the query-side builds and maintains a *materialised view* which tracks the state of the individual Todo items (in terms of whether they are complete or incomplete). The query-side can be replicated many times for scalability and the messages held by RabbitMQ are durable, so they can be stored on behalf of the query side if there is a problem.

The command-side and the query-side both have REST API's which can be used to access their capabilities.

> These REST API's are 'work in progress'.

Read the [Axon documentation](http://www.axonframework.org/download/) for the finer details of how Axon generally operates to bring you CQRS and Event Sourcing to your apps, as well as lots of detail on how it all get's configured (spoiler: it's mostly spring-context XML for the setup and some Java extensions and annotations within the code).

## Running the Demo yourself...

Assuming you already have...

- [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (I'm using v1.8.0_60)
- [Git](https://git-scm.com/) (I'm using v1.9.1)
- [Docker](https://www.docker.com/) (I'm using v1.8.2)

...then do the following...

### Step 1: Spool up the Database and Messaging servers...

First lets get the rabbit and mongo servers up. We'll use Docker because its really simple.

```bash
$ docker run -d --name my-mongo -p 27017:27017 mongo
$ docker run -d --name my-rabbit -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=test -e RABBITMQ_DEFAULT_PASS=password -e RABBITMQ_NODENAME=my-rabbit rabbitmq:3-management
$ docker ps
```

Executing these commands should pull down the required docker containers and install and run them locally. They're given the names `my-rabbit` and `my-mongo` and they'll run in the background until you ask docker to stop them (using `docker stop my-mongo` for example).

If you already have MongoDB and RabbitMQ servers available on localhost (and of the default ports) you can use those instead of docker if you have the required users and settings (see the blockquotes below for details).

> The demo expects a RabbitMQ user with the username `test` and the password `password` to be present and for this user to have admin rights so that it can create exchanges and queues. If you don't want to add such a user, stop your local RabbitMQ server and start the docker one instead.

> The demo also expects the MongoDB server to have a default `guest` user with no password and for this guest user to have admin rights. If you don't want to add such a user, stop your local MongoDB server and start the docker container instead using the commands above.

### Step 2: Clone and build the project...

Next we can download, build it and unit test the microservices project. Here I'm using the Gradle wrapper, so there is no need to actually install Gradle if you don't want to.

```bash
$ git clone https://github.com/benwilcock/microservice-sampler.git
$ cd microservice-sampler
$ ./gradlew clean test
```

### Step 3: Integration Test (using multiple VM's)...

So far so good. Now we want to test the delivery of event messages to other processes. To do this we need two (**2**) terminal windows. In one window we'll boot the `query-side` (which contains an event-listener and a materialised view), and in the other terminal we'll fire the `command-side` integration tests (which generate commands that generate events).

In **terminal 1**:
```bash
$ ./gradlew query-side:bootRun
```
After lots of output from Spring Boot, you should have a fully booted query-side microservice listening on http port 9002 (which can be changed later if you like).

In **terminal 2**:

We can check the service is started and that the *material-view* is empty by sending a RESTful query to the service like so:-

```bash
$ curl http://localhost:9002/todo/count
```

This should get a response such as `COUNT: todo=0 done=0` confirming that the query-side has not received any events yet and that the material view is empty.

```bash
$ ./gradlew command-side:integrationTest
```
The integration test sends hundreds of events generated by the command-side to the query-side (via a RabbitMQ exchange). You should see this as lots of logging output in terminal 1 while the test is in progress.

Finally, lets confirm the state of the query side.

In **terminal 2** ask the query-side microservice for a count to show how many complete and incomplete Todo items it has.

```bash
$ curl http://localhost:9002/todo/count
```
You should get an answer that is something like `COUNT: todo=200 done=100` confirming that lots of events were recieved and that the material view was altered accordingly.

