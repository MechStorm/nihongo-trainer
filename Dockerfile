FROM eclipse-temurin:21-jdk
VOLUME /tmp
WORKDIR /app
COPY target/nihongo-trainer-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]