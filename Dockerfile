# ─── Stage 1: Build ───
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven POM and pre-fetch dependencies
COPY medcare/pom.xml .
RUN mvn dependency:go-offline -B

# Copy project source and package
COPY medcare/src ./src
RUN mvn clean package -DskipTests -B

# ─── Stage 2: Run ───
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S medcare && adduser -S medcare -G medcare
USER medcare

COPY --from=build /app/target/medcare-1.0.0.jar app.jar

# Render sets the PORT env var automatically
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
