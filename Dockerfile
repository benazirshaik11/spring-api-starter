FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/store-app.jar .

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "store-app.jar"]