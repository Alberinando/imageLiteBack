FROM maven:3.8.5-openjdk-17 AS builder
LABEL authors="Alberinando"
WORKDIR /app
COPY . .
RUN mvn dependency:resolve
RUN mvn clean package -DskipTests

FROM amazoncorretto:17
WORKDIR /app
COPY --from=builder /app/target/*.jar application.jar
EXPOSE 8089

ENV POSTGRES_HOST=localhost
RUN echo "The env var POSTGRES_HOST value is $POSTGRES_HOST"

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "application.jar"]
