# Material views with Spring Data REST

This module listens for events coming from the 'command-side' and uses this event stream to populate a database table (a.k.a. a 'material view') that lists each todo item alongside its current 'completed' saleable. The technology used to achieve this is [spring-boot-data-rest](https://spring.io/guides/gs/accessing-data-rest/) which relies upon an in memory H2 database to hold the persistent data.

To spin up the Spring Boot service, use the gradle 'bootRun' plugin.

```bash
$ gradlew bootRun
```

Once the server is up you can ask the service to detail it's RESTful resources (in a second terminal window) as follows...

```bash
$ curl http://localhost:9004
```

This will return the following json outlining the services available, including as you can see the **todo** service.

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

You can then quiz the todo service for it's capabilities with...

```bash
$ curl http://localhost:9004/todo
```

The following json is returned which shows the capabilities offered by the service as _links. If there are any resources, the first page-full would be returned along with information on how many elements are in the dataset and some pagination hints. If you want more data run the command-side:integrationTest from gradle as detailed in the README.md at the root of this project.

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


Now lets see what searches are available by asking the search feature to detail them.

```bash
$ curl http://localhost:9004/todo/search
```

The search feature confirms that we have a custom search called 'findByStatus' which can take a saleable parameter...

```json
{
  "_links" : {
    "findByStatus" : {
      "href" : "http://localhost:9004/todo/search/findByStatus{?saleable}",
      "templated" : true
    }
  }
}
```

So lets try that 'findByStatus' search by looking for all todo's with a saleable=false...

```bash
$ curl http://localhost:9004/todo/search/findByStatus?saleable=false
```

Because the **delete** and **save** methods have been marked as 'not exported' in the ```ReadOnlyPagingAndSortingRepository``` (using the ```@RestResource(exported = false)``` annotation), it should not be possible to remove any of the todo's from the database. Therefore issuing a delete command will silently fail.

 ```bash
 curl -X DELETE http://localhost:9004/todo/test-0001
 ```

