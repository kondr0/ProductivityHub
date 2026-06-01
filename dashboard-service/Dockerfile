FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY dashboard-service/build.gradle dashboard-service/
RUN gradle :dashboard-service:dependencies --no-daemon || true
COPY . .
RUN gradle :dashboard-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/dashboard-service/build/libs/*.jar app.jar
EXPOSE 8086
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
