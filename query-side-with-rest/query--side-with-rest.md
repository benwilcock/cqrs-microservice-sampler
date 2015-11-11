# Spring Boot with Data and REST

This sample shows how spring data can be used with spring rest and spring boot to create a simple CRUD application.

To spin up the Spring Boot service, use the gradle 'bootRun' plugin.

```bash
$ gradlew bootRun
```

Once the server is up you can ask the service to detail it's resources...

```bash
$ curl http://localhost:8080
```

This will return the following json outlining the services available, including as you can see the
**todo** service.

```json
{
  "_links" : {
    "todo" : {
      "href" : "http://localhost:8080/todo{?page,size,sort}",
      "templated" : true
    },
    "profile" : {
      "href" : "http://localhost:8080/alps"
    }
  }
}
```

You can then quiz the todo service for it's capabilities with...

```bash
$ curl http://localhost:8080/todo
```

The following json is returned which shows the capabilities offered by the service as _links.

```json
{
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/todo{?page,size,sort}",
      "templated" : true
    },
    "search" : {
      "href" : "http://localhost:8080/todo/search"
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

Now lets create a new 'todo' item via REST (using POST). In this case I've specified an ID myself which is a bit different from the usual script. The ability to create and delete data can be swiched off using an annotation - see the ```ReadOnlyPagingAndSortingRepository``` for details.

```bash
$ curl -i -X POST -H "Content-Type:application/json" -d '{  "id": "test-0001", "title" : "Buy Milk!",  "status" : "false" }' http://localhost:8080/todo
```

The service will create the record in the H2 database (an in-memory database by default), and return the following response...
```
HTTP/1.1 201 Created
Server: Apache-Coyote/1.1
Location: http://localhost:8080/todo/test-0001
Content-Length: 0
Date: Mon, 09 Nov 2015 14:56:11 GMT
```

Now lets read the record from the database using its resource ID and view it's contents.

```bash
$ curl http://localhost:8080/todo/test-0001
```

```json
{
  "title" : "Buy Milk!",
  "status" : false,
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/todo/test-0001"
    }
  }
}
```

For comparison, we'll also create another todo with a different ID that has a status of 'true'.

```bash
$ curl -i -X POST -H "Content-Type:application/json" -d '{  "id": "test-0002", "title" : "Buy Bread!",  "status" : "true" }' http://localhost:8080/todo
```

```
HTTP/1.1 201 Created
Server: Apache-Coyote/1.1
Location: http://localhost:8080/todo/test-0002
Content-Length: 0
Date: Mon, 09 Nov 2015 15:01:08 GMT
```

Now lets see what searches are available by asking the search feature to detail them.

```bash
$ curl http://localhost:8080/todo/search
```

The search feature confirms that we have a custom search called 'findByStatus' which can take a status parameter...

```json
{
  "_links" : {
    "findByStatus" : {
      "href" : "http://localhost:8080/todo/search/findByStatus{?status}",
      "templated" : true
    }
  }
}
```

So lets try that 'findByStatus' search by looking for all todo's with a status=false...

```bash
$ curl http://localhost:8080/todo/search/findByStatus?status=false
```

The service returns the following hits to our search criteria...

```json
{
  "_embedded" : {
    "todo" : [ {
      "title" : "Buy Milk!",
      "status" : false,
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/todo/test-0001"
        }
      }
    } ]
  }
}
```

Buy Milk! is listed because it's status was **false**, but 'Buy Bread!' was not listed in the results because it's status is currently true.

Now, lets update the 'Buy Bread!' resource so that it's status becomes 'false', then re-run the search.

```bash
$ curl -X PUT -H "Content-Type:application/json" -d '{  "id": "test-0002", "title" : "Buy Bread!",  "status" : "false" }' http://localhost:8080/todo/test-0002
$ curl http://localhost:8080/todo/search/findByStatus?status=false
```

Now both 'Buy Milk!' and 'Buy Bread!' show in the 'findByStatus' search results when "status = false" is given as the search criteria.

```json
{
  "_embedded" : {
    "todo" : [ {
      "title" : "Buy Milk!",
      "status" : false,
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/todo/test-0001"
        }
      }
    }, {
      "title" : "Buy Bread!",
      "status" : false,
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/todo/test-0002"
        }
      }
    } ]
  }
}
```

Because the DELETE method has been marked as 'not exported' in the ```ReadOnlyPagingAndSortingRepository``` using the ```@RestResource(exported = false)``` annotation, it should not be possible to remove any of the todo's from the database. Isssuing a delete command silently fails.

 ```bash
 curl -X DELETE http://localhost:8080/todo/test-0001
 ```

