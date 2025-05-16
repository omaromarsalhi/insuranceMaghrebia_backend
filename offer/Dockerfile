# Stage 1: Build the app
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/offers.jar app.jar
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "app.jar"]
