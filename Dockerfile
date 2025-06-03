FROM maven:3.9.9-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

from openjdk:21-jdk-slim
COPY --from=build /target/eliza-0.0.1-SNAPSHOT.jar eliza.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "eliza.jar"]