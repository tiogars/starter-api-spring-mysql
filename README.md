# starter-api-spring-mysql

[![CI/CD Pipeline](https://github.com/tiogars/starter-api-spring-mysql/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/tiogars/starter-api-spring-mysql/actions/workflows/ci-cd.yml)
[![GitHub release](https://img.shields.io/github/v/release/tiogars/starter-api-spring-mysql)](https://github.com/tiogars/starter-api-spring-mysql/releases)
[![Docker Image](https://img.shields.io/badge/docker-ghcr.io-blue)](https://github.com/tiogars/starter-api-spring-mysql/pkgs/container/starter-api-spring-mysql)

Spring Boot + MySQL Starter API with automated CI/CD.

## 📋 Table of Contents

* [Features](#-features)
* [Requirements](#️-requirements)
* [Installation](#-installation)
* [Usage](#-usage)
* [CI/CD](#-cicd)
* [Docker](#-docker)
* [Documentation](#-documentation)

## ✨ Features

* **Spring Boot 3.5.7** with Java 21
* **REST API** with OpenAPI/Swagger documentation
* **Persistence** with Spring Data JPA and MySQL
* **Monitoring** with Spring Boot Actuator
* **Full CI/CD** with GitHub Actions
* **Containerization** with multi-stage Docker
* **Automated tests** and coverage reports
* **Security scanning** with Trivy
* **Automatic releases** with artifacts

## 🛠️ Requirements

### Local development

* Java 21+
* Maven 3.6+
* MySQL 8.0+
* Docker & Docker Compose (optional)

### Access GitHub dependencies

* GitHub account
* Personal Access Token with `read:packages`

## 📥 Installation

### 1. Quick setup (recommended)

```powershell
# Clone repository
git clone https://github.com/tiogars/starter-api-spring-mysql.git
cd starter-api-spring-mysql

# Configure Maven for GitHub Packages
./cicd.ps1 setup

# Build application
./cicd.ps1 build
```

### 2. Manual setup

```powershell
# Copy Maven settings
copy settings.xml.example $env:USERPROFILE\.m2\settings.xml

# Edit with your GitHub credentials
notepad $env:USERPROFILE\.m2\settings.xml

# Build
./mvnw.cmd clean compile
```

See [QUICKSTART.md](docs/QUICKSTART.md) for more details.

## 🚀 Usage

### Local development (repeat for quick copy)

```powershell
./mvnw.cmd compile
./mvnw.cmd test
./mvnw.cmd spring-boot:run
```

### Docker Compose (recommended)

```bash
docker-compose up -d
docker-compose logs -f
docker-compose down
```

### Access endpoints

* Swagger UI: <http://localhost:8080/swagger-ui/index.html>
* Health: <http://localhost:8080/actuator/health>
* Metrics: <http://localhost:8080/actuator/metrics>
* Info: <http://localhost:8080/actuator/info>

## 🔄 CI/CD

GitHub Actions handles build, test, release, Docker image publishing and security scanning.

Triggers:

* Push to `main` or `develop`
* Pull Request targeting `main` or `develop`
* Tags matching `v*.*.*`
* Manual dispatch

Pipeline steps:

1. Build & Test (Maven compile/test)
2. Release (on tags) – GitHub Release with artifacts
3. Docker – Multi-platform build & push (GHCR)
4. Security – Trivy vulnerability scan

Release examples:

```powershell
./cicd.ps1 release 1.0.0
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

More: [`.github/workflows/README.md`](.github/workflows/README.md).

## 🐳 Docker

```bash
docker pull ghcr.io/tiogars/starter-api-spring-mysql:latest
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/starterdb \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  --name starter-api \
  ghcr.io/tiogars/starter-api-spring-mysql:latest
```

Local image build:

```powershell
./cicd.ps1 docker-build
docker build -t starter-api:latest .
```

## 📚 Documentation

Project documentation is organized in the [`docs/`](docs/) folder:

* [QUICKSTART.md](docs/QUICKSTART.md) – Quick start guide
* [ARCHITECTURE.md](docs/ARCHITECTURE.md) – Architecture overview
* [CI-CD-SUMMARY.md](docs/CI-CD-SUMMARY.md) – CI/CD pipeline details
* [VERSION-MANAGEMENT.md](docs/VERSION-MANAGEMENT.md) – Version management strategy
* [WORKFLOW-DIAGRAM.md](docs/WORKFLOW-DIAGRAM.md) – Workflow diagrams
* [RELEASE_CHECKLIST.md](docs/RELEASE_CHECKLIST.md) – Release checklist
* [HELP.md](docs/HELP.md) – Additional help and references
* [BADGES.md](docs/BADGES.md) – Badge documentation
* [CHANGELOG-VERSION-MANAGEMENT.md](docs/CHANGELOG-VERSION-MANAGEMENT.md) – Changelog and version management

### Technologies

Includes:

* Spring Boot Actuator, DevTools, Configuration Processor
* Spring Web, Spring Data JPA, MySQL Driver
* SpringDoc OpenAPI (Swagger)

Custom dependencies (GitHub Packages):

* `architecture-create-service` (1.0.2)
* `architecture-select-service` (1.0.0)

For more information, see the workflow documentation at [`.github/workflows/README.md`](.github/workflows/README.md).

## 🤝 Contributing

```bash
git checkout -b feature/amazing-feature
git commit -m "feat: add amazing feature"
git push origin feature/amazing-feature
```

Open a Pull Request and follow Conventional Commits.

## 📄 License

See [LICENSE](LICENSE) for details.

## 👤 Author

**Tiogars** – GitHub: [@tiogars](https://github.com/tiogars)

## 🔗 Useful Links

* [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
* [GitHub Actions Docs](https://docs.github.com/actions)
* [GitHub Packages Docs](https://docs.github.com/packages)
* [Docker Hub](https://hub.docker.com/)
* [Conventional Commits](https://www.conventionalcommits.org/)

---

⭐ If this project helps you, give it a star!
