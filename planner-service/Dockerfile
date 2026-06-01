FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY planner-service/build.gradle planner-service/
RUN gradle :planner-service:dependencies --no-daemon || true
COPY . .
RUN gradle :planner-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/planner-service/build/libs/*.jar app.jar
EXPOSE 8085
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
