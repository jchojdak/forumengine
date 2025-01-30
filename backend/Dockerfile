# STAGE: run
FROM openjdk:17-jdk
WORKDIR /app
COPY backend/target/*.jar forumengine.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "forumengine.jar"]