# Use a much more secure and lightweight Alpine Linux base image with Java 17 pre-installed
FROM eclipse-temurin:17-jre-alpine

# Alpine uses 'apk' instead of 'yum'. Upgrade to ensure latest security patches.
RUN apk update && apk upgrade

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the local target directory into the container
COPY target/*.jar app.jar

# Expose port 8080 (the default port for Java web applications)
EXPOSE 8100

# The command that will execute when the container starts
CMD ["java", "-jar", "app.jar"]


