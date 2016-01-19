
# Starting the 'Command' side

To successfully start the command side of the Todo application, you need to sirst boot its docker dependencies.
```
$ ./start-servers.sh
```

# Creating Todo items using RESTful calls.

You can create a new Todo item at any time using the REST API.

```
curl -v --data "desc=blah" http://localhost:9000/create/{Insert your GUID here}
```