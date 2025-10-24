# Configuration GitHub Actions CI/CD

Ce repository utilise GitHub Actions pour automatiser le build, les tests, la création de releases et le déploiement Docker.

## 🔄 Workflows disponibles

### CI/CD Pipeline Complet (`ci-cd.yml`)

Ce workflow s'exécute sur :
- Push sur les branches `main` et `develop`
- Création de tags `v*.*.*`
- Pull requests vers `main` et `develop`
- Déclenchement manuel

#### Jobs inclus :

1. **Build and Test**
   - Configure Java 21 et Maven
   - Récupère les dépendances depuis GitHub Packages
   - Compile l'application
   - Exécute les tests
   - Upload des artifacts

2. **Create Release** (uniquement sur tags)
   - Crée une release GitHub
   - Génère un changelog automatique
   - Attache les sources et le JAR compilé

3. **Docker Build & Push**
   - Build multi-plateforme (amd64/arm64)
   - Push vers GitHub Container Registry
   - Génération du SBOM

4. **Security Scan**
   - Scan de vulnérabilités avec Trivy
   - Upload des résultats vers GitHub Security

## 📋 Prérequis

### Secrets GitHub (déjà configurés automatiquement)
- `GITHUB_TOKEN` : Token fourni automatiquement par GitHub Actions

### Configuration locale pour Maven

Pour accéder aux dépendances GitHub Packages en local, créez `~/.m2/settings.xml` :

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>VOTRE_USERNAME_GITHUB</username>
      <password>VOTRE_PERSONAL_ACCESS_TOKEN</password>
    </server>
  </servers>
</settings>
```

**Créer un Personal Access Token (PAT) :**
1. Allez sur GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Générez un nouveau token avec les permissions : `read:packages`

## 🚀 Utilisation

### Déclencher un build automatique

```bash
# Sur la branche develop ou main
git add .
git commit -m "feat: nouvelle fonctionnalité"
git push
```

### Créer une release

```bash
# Créer un tag de version
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

Le workflow va automatiquement :
1. ✅ Compiler et tester l'application
2. 📦 Créer une release GitHub avec les artifacts
3. 🐳 Build et push l'image Docker
4. 🔒 Scanner les vulnérabilités

### Déclenchement manuel

Allez sur : `Actions` → `CI/CD Pipeline` → `Run workflow`

## 🐳 Utiliser l'image Docker

### Pull depuis GitHub Container Registry

```bash
# Dernière version
docker pull ghcr.io/tiogars/starter-api-spring-mysql:latest

# Version spécifique
docker pull ghcr.io/tiogars/starter-api-spring-mysql:v1.0.0

# Branche develop
docker pull ghcr.io/tiogars/starter-api-spring-mysql:develop
```

### Lancer l'application

```bash
# Avec docker-compose (recommandé)
docker-compose up -d

# Ou manuellement
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/starterdb \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  --name starter-api \
  ghcr.io/tiogars/starter-api-spring-mysql:latest
```

## 📊 Artifacts disponibles

Après chaque build réussi, vous trouverez :

### Dans GitHub Actions
- **build-artifacts** : JAR compilé (7 jours)
- **test-results** : Rapports de tests (7 jours)
- **sbom** : Software Bill of Materials (90 jours)

### Dans GitHub Releases (pour les tags)
- `starter-VERSION.jar` : Application compilée
- `starter-api-VERSION-sources.zip` : Code source (ZIP)
- `starter-api-VERSION-sources.tar.gz` : Code source (TAR.GZ)

### Dans GitHub Container Registry
- Images Docker avec tags multiples
- Support multi-architecture (amd64, arm64)

## 🔐 Sécurité

### Scan de vulnérabilités
Chaque image Docker est scannée avec Trivy pour détecter :
- Vulnérabilités CRITICAL et HIGH
- Résultats disponibles dans l'onglet Security

### SBOM (Software Bill of Materials)
Un SBOM au format CycloneDX est généré pour chaque build et conservé 90 jours.

## 🏷️ Convention de versioning

Le projet utilise Semantic Versioning (SemVer) :
- `v1.0.0` : Release stable
- `v1.0.0-RC1` : Release candidate (prerelease)
- `v1.0.0-SNAPSHOT` : Version de développement (prerelease)

### Tags Docker générés automatiquement
- `latest` : Dernière version de la branche main
- `v1.0.0` : Tag de version exacte
- `1.0` : Tag majeur.mineur
- `1` : Tag majeur
- `develop` : Branche develop
- `main-abc1234` : SHA du commit sur main

## 📝 Notes importantes

1. **Dépendances GitHub** : Les dépendances `architecture-create-service` et `architecture-select-service` doivent être disponibles sur GitHub Packages du compte `tiogars`

2. **Cache Maven** : Le cache Maven est géré automatiquement par GitHub Actions pour accélérer les builds

3. **Multi-platform builds** : Les images Docker sont buildées pour AMD64 et ARM64

4. **Tests** : Les tests doivent passer pour que le workflow continue (sauf build Docker qui peut continuer)

## 🔧 Maintenance

### Mettre à jour les dépendances

```bash
./mvnw versions:display-dependency-updates
./mvnw versions:use-latest-releases
```

### Nettoyer les anciens artifacts

Les artifacts sont automatiquement supprimés après :
- 7 jours pour build-artifacts et test-results
- 90 jours pour les SBOM

## 📚 Ressources

- [GitHub Actions Documentation](https://docs.github.com/actions)
- [GitHub Packages Maven](https://docs.github.com/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)
- [GitHub Container Registry](https://docs.github.com/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
- [Spring Boot with Docker](https://spring.io/guides/topicals/spring-boot-docker)
