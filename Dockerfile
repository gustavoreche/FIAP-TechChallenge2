FROM openjdk:17-jdk-alpine
MAINTAINER greche96
COPY /target/tech-challenge-2-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]