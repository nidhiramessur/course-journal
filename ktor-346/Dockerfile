# Use a lightweight version of OpenJDK
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your local build directory to the container
COPY ./build/libs/com.example.ktor-346-all.jar /app/ktor-346.jar

# Copy your Firebase key file into the container
COPY ./src/main/kotlin/com/example/firebase-key.json /app/firebase-key.json

# Expose the port your application runs on
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "/app/ktor-346.jar"]
