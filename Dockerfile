# Usa una imagen oficial de OpenJDK como base
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el JAR de tu aplicación al contenedor
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Expone el puerto en el que la aplicación se ejecutará
EXPOSE 8080

# Define el comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","app.jar"]