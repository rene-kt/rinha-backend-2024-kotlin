FROM openjdk:17-jdk-slim
EXPOSE 8080
ADD /build/libs/starter-1.0.0-SNAPSHOT-fat.jar rinha-fat.jar
ENTRYPOINT ["java", "-jar", "rinha-fat.jar", "run", "com.example.starter.MainVerticle"]
