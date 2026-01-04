FROM  maven:3.9-amazoncorretto-21-al2023 AS dependency-builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

FROM  maven:3.9-amazoncorretto-21-al2023 AS builder
WORKDIR /app
COPY --from=dependency-builder /root/.m2 /root/.m2
COPY src src
COPY pom.xml .
RUN mvn package -DskipTests -B

FROM amazoncorretto:21.0.9-al2023-headless
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]