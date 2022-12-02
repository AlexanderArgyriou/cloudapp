FROM adoptopenjdk/openjdk11:jdk-11.0.7_10-alpine as builder

ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

EXPOSE 8080