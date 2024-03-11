FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/NbpCurrencyRatesService-0.0.1-SNAPSHOT.jar /app/my-app.jar
CMD ["java", "-jar", "/app/my-app.jar"]
EXPOSE 8080
