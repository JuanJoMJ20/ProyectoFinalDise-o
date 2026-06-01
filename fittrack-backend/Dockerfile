# ==========================================
# Build Stage (Etapa de compilación)
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar la caché de capas de Docker en las dependencias
COPY pom.xml .
# Descargamos las dependencias sin compilar el código fuente (se hace offline)
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos la aplicación saltando los tests para acelerar el build en el CI/CD
RUN mvn clean package -DskipTests

# ==========================================
# Run Stage (Etapa de ejecución en producción)
# ==========================================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Opcional: Crear un usuario sin privilegios por seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiamos únicamente el .jar generado en la etapa anterior (esto mantiene la imagen final muy ligera)
COPY --from=builder /app/target/fittrack-backend-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto que utiliza nuestra app (puede ser sobreescrito por la variable $PORT)
EXPOSE 8080

# Comando para ejecutar la aplicación, activando explícitamente el perfil "prod"
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
