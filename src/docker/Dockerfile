FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=./challenge-app/target/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
EXPOSE 8080