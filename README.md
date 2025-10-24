# starter-api-spring-mysql

[![CI/CD Pipeline](https://github.com/tiogars/starter-api-spring-mysql/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/tiogars/starter-api-spring-mysql/actions/workflows/ci-cd.yml)
[![GitHub release](https://img.shields.io/github/v/release/tiogars/starter-api-spring-mysql)](https://github.com/tiogars/starter-api-spring-mysql/releases)
[![Docker Image](https://img.shields.io/badge/docker-ghcr.io-blue)](https://github.com/tiogars/starter-api-spring-mysql/pkgs/container/starter-api-spring-mysql)

Spring Boot + MySQL Starter API avec CI/CD automatisé

## 📋 Table des matières

- [Fonctionnalités](#fonctionnalités)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [CI/CD](#cicd)
- [Docker](#docker)
- [Documentation](#documentation)

## ✨ Fonctionnalités

- **Spring Boot 3.5.7** avec Java 21
- **API REST** avec documentation OpenAPI/Swagger
- **Persistance** avec Spring Data JPA et MySQL
- **Monitoring** avec Spring Boot Actuator
- **CI/CD complet** avec GitHub Actions
- **Containerisation** avec Docker multi-stage
- **Tests automatisés** et rapports de couverture
- **Scan de sécurité** avec Trivy
- **Releases automatiques** avec artifacts

## 🛠️ Prérequis

### Pour le développement local
- Java 21+
- Maven 3.6+
- MySQL 8.0+
- Docker & Docker Compose (optionnel)

### Pour accéder aux dépendances GitHub
- Un compte GitHub
- Un Personal Access Token avec permission `read:packages`

## 📥 Installation

### 1. Configuration rapide (recommandé)

```powershell
# Cloner le repository
git clone https://github.com/tiogars/starter-api-spring-mysql.git
cd starter-api-spring-mysql

# Configurer Maven pour GitHub Packages
.\cicd.ps1 setup

# Compiler l'application
.\cicd.ps1 build
```

### 2. Configuration manuelle

```powershell
# Copier le fichier de configuration Maven
copy settings.xml.example $env:USERPROFILE\.m2\settings.xml

# Éditer et configurer avec vos credentials GitHub
notepad $env:USERPROFILE\.m2\settings.xml

# Compiler
.\mvnw.cmd clean compile
```

Voir [QUICKSTART.md](QUICKSTART.md) pour plus de détails.

## 🚀 Utilisation

### Développement local

```bash
# Compiler
.\mvnw.cmd compile

# Exécuter les tests
.\mvnw.cmd test

# Lancer l'application
.\mvnw.cmd spring-boot:run
```

### Avec Docker Compose (recommandé)

```bash
# Lancer l'application et MySQL
docker-compose up -d

# Voir les logs
docker-compose logs -f

# Arrêter
docker-compose down
```

### Accéder à l'application

- **API Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Health Check**: http://localhost:8080/actuator/health
- **Métriques**: http://localhost:8080/actuator/metrics
- **Info**: http://localhost:8080/actuator/info

## 🔄 CI/CD

Ce projet utilise GitHub Actions pour l'intégration et le déploiement continus.

### Pipeline automatique

Le workflow CI/CD se déclenche automatiquement sur :
- **Push** vers `main` ou `develop`
- **Pull Requests** vers `main` ou `develop`
- **Tags** au format `v*.*.*`
- **Déclenchement manuel**

### Étapes du pipeline

1. ✅ **Build & Test** - Compilation et tests avec Maven
2. 📦 **Release** - Création de release GitHub avec artifacts (sur tags)
3. 🐳 **Docker** - Build et push vers GitHub Container Registry
4. 🔒 **Security** - Scan de vulnérabilités avec Trivy

### Créer une release

```powershell
# Avec le script PowerShell (recommandé)
.\cicd.ps1 release 1.0.0

# Ou manuellement
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

📚 **Documentation complète** : [.github/workflows/README.md](.github/workflows/README.md)

## 🐳 Docker

### Utiliser l'image depuis GitHub Container Registry

```bash
# Pull de l'image
docker pull ghcr.io/tiogars/starter-api-spring-mysql:latest

# Lancer avec variables d'environnement
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/starterdb \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  --name starter-api \
  ghcr.io/tiogars/starter-api-spring-mysql:latest
```

### Build local

```powershell
# Avec le script PowerShell
.\cicd.ps1 docker-build

# Ou manuellement
docker build -t starter-api:latest .
```

## 📚 Documentation

### Technologies utilisées

Ce projet a été créé avec [Spring Initializr](https://start.spring.io/) et inclut :

- **Spring Boot Actuator** - Monitoring et métriques
- **Spring Boot DevTools** - Outils de développement
- **Spring Configuration Processor** - Traitement de configuration
- **Spring Web** - API REST
- **MySQL Driver** - Connecteur MySQL
- **Spring Data JPA** - Persistance des données
- **SpringDoc OpenAPI** - Documentation API ([springdoc.org](https://springdoc.org/#getting-started))

### Dépendances personnalisées

- `architecture-create-service` (v1.0.2) - Service de création
- `architecture-select-service` (v1.0.0) - Service de sélection

Ces dépendances sont hébergées sur GitHub Packages.

### Guides utiles

- 📖 [Guide de démarrage rapide](QUICKSTART.md)
- 🔧 [Documentation CI/CD](.github/workflows/README.md)
- 🐳 [Dockerfile](Dockerfile)
- 🔑 [Configuration Maven](settings.xml.example)

## 🤝 Contribution

1. Fork le projet
2. Créez une branche (`git checkout -b feature/amazing-feature`)
3. Committez vos changements (`git commit -m 'feat: add amazing feature'`)
4. Poussez vers la branche (`git push origin feature/amazing-feature`)
5. Ouvrez une Pull Request

Suivez les [Conventional Commits](https://www.conventionalcommits.org/) pour vos messages de commit.

## 📄 Licence

Ce projet est sous licence libre. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 👤 Auteur

**Tiogars**
- GitHub: [@tiogars](https://github.com/tiogars)
- Repository: [starter-api-spring-mysql](https://github.com/tiogars/starter-api-spring-mysql)

## 🔗 Liens utiles

- [Documentation Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [GitHub Actions](https://docs.github.com/actions)
- [GitHub Packages](https://docs.github.com/packages)
- [Docker Hub](https://hub.docker.com/)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

⭐ Si ce projet vous aide, n'hésitez pas à lui donner une étoile !
