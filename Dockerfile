# Dockerfile optimized for pre-built JAR
# Uses Java 21 (Eclipse Temurin)
# This Dockerfile expects a pre-built JAR file to be present

FROM eclipse-temurin:21-jre-alpine AS runtime

# Métadonnées
LABEL maintainer="tiogars"
LABEL description="Starter API Spring Boot with MySQL"

# Créer un utilisateur non-root pour des raisons de sécurité
RUN addgroup -S spring && adduser -S spring -G spring

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR pré-construit
# Le JAR doit être dans le contexte de build (copié depuis les artifacts)
# Pattern *.jar will match the executable JAR but not *.jar.original files
COPY target/*.jar app.jar

# Changer le propriétaire des fichiers
RUN chown -R spring:spring /app

# Basculer vers l'utilisateur non-root
USER spring:spring

# Exposer le port de l'application
EXPOSE 8080

# Healthcheck pour Docker
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Point d'entrée de l'application avec optimisations JVM
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "app.jar"]
