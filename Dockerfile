
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/eliza-0.0.1-SNAPSHOT.jar eliza.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "eliza.jar"]
