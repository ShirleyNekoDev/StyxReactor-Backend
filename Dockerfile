FROM maven:3.6.1-jdk-8-slim as build
WORKDIR /totally-not-atlasreactor-server
COPY pom.xml pom.xml
COPY src src
COPY conf conf
RUN mvn package

FROM openjdk:8-jdk-slim
WORKDIR /totally-not-atlasreactor-server
COPY --from=build /totally-not-atlasreactor-server/target/totally-not-atlasreactor-server-1.0.0.jar app.jar
COPY conf conf
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
