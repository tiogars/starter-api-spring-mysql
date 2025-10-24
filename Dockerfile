# Dockerfile multi-stage pour Spring Boot
# Utilise Java 21 (Eclipse Temurin) et Maven

# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

# Métadonnées
LABEL maintainer="tiogars"
LABEL description="Starter API Spring Boot with MySQL"

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers Maven wrapper et pom.xml pour exploiter le cache Docker
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Télécharger les dépendances (cette couche sera mise en cache si pom.xml ne change pas)
RUN ./mvnw dependency:go-offline -B

# Copier le code source
COPY src ./src

# Construire l'application (skip tests pour accélérer le build, lancez les tests avant de build)
RUN ./mvnw clean package -DskipTests -B

# Extraire les layers du JAR pour optimiser les layers Docker
RUN mkdir -p target/dependency && \
    java -Djarmode=layertools -jar target/*.jar extract --destination target/dependency

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine AS runtime

# Créer un utilisateur non-root pour des raisons de sécurité
RUN addgroup -S spring && adduser -S spring -G spring

# Définir le répertoire de travail
WORKDIR /app

# Copier les layers extraits depuis le stage builder
COPY --from=builder /app/target/dependency/dependencies/ ./
COPY --from=builder /app/target/dependency/spring-boot-loader/ ./
COPY --from=builder /app/target/dependency/snapshot-dependencies/ ./
COPY --from=builder /app/target/dependency/application/ ./

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
    "org.springframework.boot.loader.launch.JarLauncher"]
