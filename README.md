# testcontainers-examples

## Running the tests with testcontainers

Open the testfile in your IDE and run the test.
Since this is an example the tests will only check that the app is started. The tests do not interact with the app. 

## Running the test app


```shell 

docker-compose up -d

```

On http://localhost:8888 (chronograf) use the name of the influxdb container (**influxdb**) instead of **localhost** in the connection url