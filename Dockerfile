# Etapa de compilación
FROM gradle:7.5.1-jdk17 AS build

# Establece el directorio de trabajo en el contenedor
WORKDIR /app

# Copia el código fuente al contenedor
COPY . .

# Compila la aplicación
RUN gradle build --no-daemon

# Etapa de ejecución
FROM openjdk:17-jdk-slim

# Copia el JAR de la aplicación desde la etapa de compilación
COPY --from=build /app/build/libs/client-management-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Define el comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","/app.jar"]