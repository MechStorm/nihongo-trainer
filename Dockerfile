#build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:resolve
COPY . .
RUN ./mvnw clean install -DskipTests

#launch
FROM eclipse-temurin:21-jdk
VOLUME /tmp
WORKDIR /app
COPY --from=build /app/target/nihongo-trainer-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]