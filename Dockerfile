FROM openjdk:8-jdk-alpine
VOLUME /tmp
ENV SPRING_DATA_MONGODB_URI "mongodb+srv://root:root@cluster0-kiouo.gcp.mongodb.net/test?retryWrites=true&w=majority"
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8080
