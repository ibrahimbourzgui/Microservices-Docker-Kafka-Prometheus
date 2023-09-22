# Microservices-Docker-Kafka-Prometheus
  <h1>Project architecture</h1>
<img src="https://github-production-user-asset-6210df.s3.amazonaws.com/47062719/269924631-f7f748e7-408d-4cab-81c9-dbc5a7d87d75.png" alt="Project architecture">
<h1>Project description and overview</h1>
<p>This is a Spring Boot project using microservices architecture. <br>
  The focus is on building services that communicate with each other using synchronous and asynchronous communication. <br>
  Eureka server is an application that holds the information about all client-service applications.<br>
  Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address.<br>
  Eureka Server is also known as Discovery Server.<br>
  Api-Getway is a server (or L7 proxy) between a client and microservices that acts as a centralized entry point for all clients into the system.<br>
  It is a reverse proxy that accepts client API calls and forwards them to the appropriate microservice.<br>
  Circuit Breaker circuit breaker to prevent slow calls between services.<br>
  The circuit breaker can be set to open or closed states, and will allow or not allow calls from the order service to the inventory service <br>
  <h3>Event-Driven architecture (Kafka)</h3>
  Kafka is used in this project to develop a Kafka-based message solutions, by configuring the Kafka broker on the local machine, and send messages to a Kafka topic using the Kafka send method. <br>
  The order service project is configured with Kafka to send orders placed events as messages to a notification topic. <br>
  The notification service project is then created, and the consumer is configured with the Kafka broker.
  <h3>Docker</h3>
   The API Gateway project is Dockerized, by creating a Dockerfile and build it. The Dockerfile uses an existing image, openjdk17, as a base.<br>
   And then I improved the build process by using a multi-stage build. <br>
    This project also includes the jib Maven plugin, configuring it for use with the project's base image, and building and pushing the resulting Docker images to Docker Hub<a href="https://hub.docker.com/u/ibrbou3">Here is the link of my Docker Hub</a>.<br>
    Then set up, required environment variables, ports, volumes, and  configure Prometheus and Grafana, for the services, as well as run the services using <Strong>Docker Compose</Strong>.
    <h3>Monitoring</h3>
    I set up monitoring for the microservice project using Prometheus and Grafana. <br>
    This project includes also an Actuator endpoint for monitoring and managing the microservice.


