# Setup Documentation

This section contains documentation for setting up and configuring the project.

## Available Documentation

### [GitHub Packages Authentication Setup](GITHUB_PACKAGES_SETUP.md)

Complete guide for authenticating with GitHub Packages to access Maven dependencies.

**Topics covered:**

- GitHub Actions workflow authentication
- Local development setup with Personal Access Tokens
- Troubleshooting authentication issues
- Security best practices

### [Docker Deployment Guide](DOCKER_DEPLOYMENT.md)

Guide for deploying the application using Docker Compose, including Dokploy deployments.

**Topics covered:**

- Environment variable configuration
- Docker Compose deployment
- Dokploy deployment
- Database connection troubleshooting
- Health checks and monitoring
- Persistent data management

---

## Quick Start

### Local Development with Maven

For local development, the most common setup task is configuring Maven to access GitHub Packages:

1. Copy the settings template: `cp settings.xml.example ~/.m2/settings.xml`
2. Create a GitHub Personal Access Token with `read:packages` permission
3. Update `~/.m2/settings.xml` with your GitHub username and token
4. Run `mvn clean install` to verify

See [GitHub Packages Setup](GITHUB_PACKAGES_SETUP.md) for detailed instructions.

### Docker Deployment

To deploy using Docker Compose:

1. Copy the environment template: `cp .env.example .env`
2. Configure database credentials in `.env`
3. Run `docker compose up -d`
4. Access the API at `http://localhost:8080/actuator/health`

See [Docker Deployment Guide](DOCKER_DEPLOYMENT.md) for detailed instructions.
