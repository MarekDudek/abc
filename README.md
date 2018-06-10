
# Collibra coding challenge

## The code

The code is in ```coding-challenge.zip``` archive, full Maven project.

## Full built package

The full built package is ```coding-challenge-1.0.0.jar```

## How to execute

Run 
```
    java -jar path/to/coding-challenge-1.0.0.jar
```

## Expected results

On my machine all six phases pass. The test takes almost exactly one minute to finish.
 
## Notes

Running another test with test client requires restarting the server. 
Server was developed with assumption that all clients operate on the same instance of graph. 
Test run can generate the same node IDs. 
Opposite assumption was tested and it failed in Phases 5 and 6.
 
The application uses SLF4J library for logging. 
You can configure logging with system properties, as described
[here](https://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html), 
for example changing log level looks like this:

```
java -Dorg.slf4j.simpleLogger.defaultLogLevel=debug -jar path/to/coding-challenge-1.0.0.jar
```