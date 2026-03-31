# ---------- BUILD STAGE ----------
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy pom.xml (cache dependencies)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy source code
COPY src src

# Build JAR
RUN mvn -B clean package -DskipTests


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy generated JAR (use wildcard to avoid name issues)
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]