FROM gradle:8.5-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY finance-service/build.gradle finance-service/
RUN gradle :finance-service:dependencies --no-daemon || true
COPY . .
RUN gradle :finance-service:bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
COPY --from=build /app/finance-service/build/libs/*.jar app.jar
EXPOSE 8083
USER appuser
ENTRYPOINT ["java", "-jar", "app.jar"]
