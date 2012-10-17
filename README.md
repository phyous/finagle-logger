finagle-logger
=====

A foray into async programming with finagle.

This is code for a finagle(http://twitter.github.com/finagle/) based webserver that accepts post requests and logs them
to a file. Was able to get up to 3.5k RPS running on localhost using jmeter on my 2011 MBP. Well done Twitter.

**Usage Instructions**

1. Make sure maven is installed on your system
> See http://maven.apache.org/download.html

2. Build code
>mvn package

3. Run webserver
>java -jar target/finagle-logger-1.0.jar

4. Make a POST request to /log endpoint (using curl or something similar)
>curl -X POST -d "Awesome data to be logged" "0.0.0.0:8880/log"

5. Observe the beautifully logged data
> Log data: logs/message.log
> Server request logging: logs/server.log