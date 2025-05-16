FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/offers.jar app.jar
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "app.jar"]
