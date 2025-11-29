FROM eclipse-temurin:21-jre-alpine@sha256:latest AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine@sha256:latest
WORKDIR /app
COPY --from=builder /app/target/UserService-0.0.1-SNAPSHOT.jar UserService.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "UserService.jar"]