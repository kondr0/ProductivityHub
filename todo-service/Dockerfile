FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY todo-service/build.gradle todo-service/
RUN gradle :todo-service:dependencies --no-daemon || true
COPY . .
RUN gradle :todo-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/todo-service/build/libs/*.jar app.jar
EXPOSE 8082
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
