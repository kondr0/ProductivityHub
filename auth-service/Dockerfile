FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY auth-service/build.gradle auth-service/
RUN gradle :auth-service:dependencies --no-daemon || true
COPY . .
RUN gradle :auth-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/auth-service/build/libs/*.jar app.jar
EXPOSE 8081
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
