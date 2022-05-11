FROM maven:3.8.5-openjdk-17-slim AS build

COPY . /app
WORKDIR /app

RUN mvn -f /app/pom.xml clean package

RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../timbuctoo-storage-tests-1.0-SNAPSHOT.jar)

FROM openjdk:17-jdk-slim

COPY --from=build /app/target/dependency/ /app

ENTRYPOINT ["java", "-cp", "/app", "nl.knaw.huc.timbuctoo.Main"]
