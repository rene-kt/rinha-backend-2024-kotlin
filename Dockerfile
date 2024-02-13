FROM openjdk:17-jdk-slim
EXPOSE 8080
ADD /build/libs/rinhadebackend-0.0.1-SNAPSHOT.jar rinha.jar
ENTRYPOINT ["java", "-jar", "rinha.jar"]