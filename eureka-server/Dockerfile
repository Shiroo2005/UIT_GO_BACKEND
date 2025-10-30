# ============================
# BUILD STAGE
# ============================
FROM eclipse-temurin:17-jdk AS build

WORKDIR /build

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

RUN chmod +x gradlew

COPY ./src ./src

RUN ./gradlew clean bootJar --no-daemon


# ============================
# APP STAGE
# ============================
FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

RUN mkdir -p /app/uploads && chown -R appuser:appgroup /app

COPY --from=build /build/build/libs/*.jar app.jar

USER appuser

EXPOSE 8761

CMD ["java", "-jar", "app.jar"]
