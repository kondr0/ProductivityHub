FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY module-registry-service/build.gradle module-registry-service/
RUN gradle :module-registry-service:dependencies --no-daemon || true
COPY . .
RUN gradle :module-registry-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/module-registry-service/build/libs/*.jar app.jar
EXPOSE 8087
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
