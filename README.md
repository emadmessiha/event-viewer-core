# Event Viewer Core

Java Spring Boot Restful application serving event logs

## Getting started

### Prerequisites

You will need to have Java JDK 8, Docker (Community Edition should be fine), and Docker Compose installed on your machine. You will also need a text/code editor. In this case Visual Studio Code is used. But you are free to use any Code Editor or IDE that can work with Spring Boot, Java, and Maven.

### Running the code

To run the code, use the following command from a terminal/command line

1. Start the database (MongoDB) using `docker-compose up` (keep that terminal open). This will start a docker container running MongoDB on port 27017 as well as a web portal (Mongo Express) to browse that database: <http://localhost:8081/>
2. Once the databas is up, run the spring boot application using `./mvnw spring-boot:run`

The application should now be running on <http://localhost:8080>

To shutdown the server, simply hit Ctrl+C on your keyboard. To shutdown the database, hit Ctrl+C then run `docker-compose down`

### Working with the code

You can edit the code through Visual Studio code by using the VS code Workspace file: `vscode.workspace`. There are recommended extensions that should help with development if you choose to use them. Another alternative to work with the code is to import it as a Maven Project in Eclipse or Spring Tool Suite.

## Docker build/run

1. Make sure you package the application first by running from within the project directory:
   `./mvnw clean install`
2. Then run the Docker build command:
   `docker build -t event-viewer-core -f Dockerfile_ev_core`
3. Lastly, run the following command to start the application through the built Docker image:
   `docker run -p 8080:8080 event-viewer-core`
   NOTE: Make sure the application is not already running through the IDE or another process. Otherwise there will be a port conflict.

You can browse the application through <http://localhost:8080>
