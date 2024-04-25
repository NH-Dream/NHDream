FROM openjdk:21

WORKDIR /app

ARG JAR_FILE=build/libs/nhdream-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} nhdream.jar

ENTRYPOINT ["java", "-jar", "nhdream.jar"]