# starter-api-spring-mysql

Spring Boot + MySQL Starter API with automated CI/CD.

[![Build develop](https://github.com/tiogars/starter-api-spring-mysql/actions/workflows/build.yml/badge.svg)](https://github.com/tiogars/starter-api-spring-mysql/actions/workflows/build.yml)
[![Docker Image](https://img.shields.io/badge/docker-ghcr.io-blue)](https://github.com/tiogars/starter-api-spring-mysql/pkgs/container/starter-api-spring-mysql)

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

### Access endpoints

* Swagger UI: <http://localhost:8080/swagger-ui/index.html>
* Health: <http://localhost:8080/actuator/health>

## 🔄 CI/CD

GitHub Actions handles build, test, release, Docker image publishing and security scanning.

Triggers:

* Push to `main` or `develop`
* Pull Request targeting `main` or `develop`
* Tags matching `v*.*.*`
* Manual dispatch

## 🔗 Useful Links

* [Spring Boot Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/)
* [GitHub Actions Docs](https://docs.github.com/actions)
* [GitHub Packages Docs](https://docs.github.com/packages)
* [Docker Hub](https://hub.docker.com/)
* [Conventional Commits](https://www.conventionalcommits.org/)
