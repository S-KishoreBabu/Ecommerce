# Builder stage - build the Spring Boot jar
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /build

# Copy only what we need first to leverage layer caching
COPY pom.xml .
# If you have a multi-module or additional files, copy them as needed
COPY src ./src

# Build (skip tests for faster builds; remove -DskipTests if you want tests)
RUN mvn -B -DskipTests package

# Runtime stage - smaller image
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app

# Copy jar from builder stage (artifactId from pom: ecommerce_backend)
COPY --from=builder /build/target/ecommerce_backend-0.0.1-SNAPSHOT.jar ./ecommerce_backend.jar

EXPOSE 8080
# Use JAVA_OPTS for memory tuning if needed
ENV JAVA_OPTS=""

CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/ecommerce_backend.jar"]
