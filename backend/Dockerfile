# STAGE: run
FROM openjdk:17-jdk
COPY target/*.jar forumengine.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "forumengine.jar"]