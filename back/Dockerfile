FROM maven:3.8.4-openjdk-17-slim AS maven_deps

COPY pom.xml /home/application/

RUN mvn -DskipTests=true -f /home/application/pom.xml dependency:go-offline

FROM maven:3.8.4-openjdk-17-slim AS build

COPY --from=maven_deps /root/.m2 /root/.m2

COPY src /home/application/src
COPY pom.xml /home/application/

ENV SPRING_PROFILES_ACTIVE=dev

RUN mvn -DskipTests=true -f /home/application/pom.xml clean package

#FROM openjdk:17-jdk-slim
FROM bellsoft/liberica-openjdk-alpine:17.0.7-7

COPY --from=build /home/application/target/imkerBack.jar /usr/local/lib/imkerBack.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/usr/local/lib/imkerBack.jar"]
