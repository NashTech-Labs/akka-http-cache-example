
# akka-http-cache-example

In real time application, we have lots of request which are very expensive. This makes application little slower. 
To avoid expensive computation again and again, akka http supports caching.

# Clone the repo


``https://github.com/knoldus/akka-http-cache-example.git``

``cd akka-http-cache-example``

# Build the application

``sbt clean compile``

# Run the application

``sbt run``

# Hit the below URL

``http://localhost:8080/factorial/200000`` // hit the url twice for big number, see the logs for time taken to execute same request

