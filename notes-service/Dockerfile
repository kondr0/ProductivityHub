FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY notes-service/build.gradle notes-service/
RUN gradle :notes-service:dependencies --no-daemon || true
COPY . .
RUN gradle :notes-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/notes-service/build/libs/*.jar app.jar
EXPOSE 8084
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
