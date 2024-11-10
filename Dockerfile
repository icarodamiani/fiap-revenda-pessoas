FROM openjdk:17

VOLUME /pessoas
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Dspring.profiles.active=local","-jar","app.jar"]
