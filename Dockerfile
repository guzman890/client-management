# Usa una imagen oficial de OpenJDK como base
FROM openjdk:17-jdk-slim

# Copia el JAR de tu aplicación al contenedor
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Define el comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app.jar"]