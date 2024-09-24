# First stage: Build the application using Maven
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copy the source code inside src directory and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Second stage: Create a image to run the application
FROM openjdk:17-jdk-slim

# Copy the Spring Boot JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Start the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app.jar"]