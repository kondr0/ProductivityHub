FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gateway-service/build.gradle gateway-service/
RUN gradle :gateway-service:dependencies --no-daemon || true
COPY . .
RUN gradle :gateway-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/gateway-service/build/libs/*.jar app.jar
EXPOSE 8080
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
