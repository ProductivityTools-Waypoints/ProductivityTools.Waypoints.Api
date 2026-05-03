# Build stage
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copy gradle wrapper and build files first to cache dependencies
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# Copy source code and build the application
COPY src src
RUN ./gradlew bootJar --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*[!-plain].jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
