FROM gradle:8.9-jdk21-alpine AS builder
LABEL authors="Erin MacKenzie"

WORKDIR /home/gradle/src

# Copy Gradle configuration
COPY settings.gradle build.gradle gradle.properties* ./
COPY gradle ./gradle

# Pre-fetch dependencies
RUN gradle --no-daemon dependencies || true

# Copy the rest of the project
COPY . .

# Build the application (skip tests)
RUN gradle --no-daemon clean build -x test

# Runtime stage: run on a slim JRE 21 image
FROM eclipse-temurin:21-jre-alpine
ENV APP_HOME=/app
WORKDIR ${APP_HOME}

# Copy the built jar
COPY --from=builder /home/gradle/src/app/build/libs/tavern-*.jar tavern.jar

# Expose ports
# None for now
# EXPOSE 8080

# Run as non-root for better security
RUN addgroup -S app && adduser -S app -G app
USER app

# Start the application
ENTRYPOINT ["java","-jar","/app/app.jar"]
