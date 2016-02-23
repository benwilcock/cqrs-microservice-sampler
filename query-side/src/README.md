# Query Side with REST

The query side of this CQRS demo is listening for events coming from the command-side and uses them to drive a material-view of the 'todo' items. The events are delivered inter-process via AMQP (RabbitMQ) and populate aan in memory database (H2). This can be queried via REST thanks to Spring Boot Data Rest.

To spin up the Spring Boot service, use the gradle 'bootRun' plugin.

```bash
$ gradlew bootRun
```

Once the server is up you can ask the service to detail it's resources...

```bash
$ curl http://localhost:9004
```

This will return the following JSON outlining the services available, including as you can see, the **todo** service.

```json
{
  "_links" : {
    "todo" : {
      "href" : "http://localhost:9004/todo{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:9004/alps"
    }
  }
}
```

You can then quiz the todo service for it's capabilities and contents with...

```bash
$ curl http://localhost:9004/todo
```

The following json is returned which shows the capabilities offered by the service as _links, and a list of the first page of todo results (if any exist).

```json
{
  "_links" : {
    "self" : {
      "href" : "http://localhost:9004/todo{?page,size,sort}",
      "templated" : true
    },
    "search" : {
      "href" : "http://localhost:9004/todo/search"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 0,
    "totalPages" : 0,
    "number" : 0
  }
}
```


Because the **delete** and **save** methods of the repository interface have been marked as 'not exported' in the ```ReadOnlyPagingAndSortingRepository``` (using the ```@RestResource(exported = false)``` annotation), it should not be possible to remove any of the todo's from the database. For example, issuing a delete command silently fails.

 ```bash
 curl -X DELETE http://localhost:9004/todo/{ID}
 ```

