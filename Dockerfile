# Stage 1: Build dengan Maven + JDK 17 (Alpine)
FROM maven:3.8.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Runtime dengan JRE 17 (Alpine)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy all JAR files from target (except *-sources.jar, etc.)
COPY --from=builder /app/target/*.jar app.jar
COPY src/main/resources/keystore.jks .
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]