FROM openjdk:21-jdk-slim

WORKDIR /app


COPY target/*.jar app.jar

# Mở port 8080
EXPOSE 8088

# Chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]