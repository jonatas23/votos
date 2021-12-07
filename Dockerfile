FROM openjdk:8-jre-alpine

WORKDIR /

COPY target/votos-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005", "-jar", "app.jar"]
