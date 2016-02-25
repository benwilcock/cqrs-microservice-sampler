## Integration Test Instructions

These integration tests need the __command-side__ and __query-side__ app containers to be UP and running. In turn these apps also depend on the MongoDB and RabbitMQ servers being available.

To start a test run use the following commands...

```bash
docker start my-mongo
docker start my-rabbbit
```

Then in one terminal...

```bash
./gradlew command-side:bootRun
```

Then in another terminal...

```bash
./gradlew query-side:bootRun
```

Then finally in a third terminal run the integration tests...

```bash
./gradlew integration-test:integrationTest
```

The tests use ***RestAssured*** to send commands to the command side before following up on their success by sending queries to the query-side. Because the app features 'eventual-consistency' a small delay is introduced between each test to give time for the event messages to propagate from the command-side to the query-side.

The tests also use the @FixMethodOrder annotation from JUnit to allow the tests to be a little bit more modular but still execute in the correct order for testing.



