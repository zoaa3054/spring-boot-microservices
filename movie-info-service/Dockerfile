FROM maven:3.9.9-eclipse-temurin-11 AS builder
WORKDIR /workspace

COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:11-jre
WORKDIR /app

COPY --from=builder /workspace/target/*.jar /app/app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
