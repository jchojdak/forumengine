# STAGE: run
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/*.jar forumengine.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "forumengine.jar"]