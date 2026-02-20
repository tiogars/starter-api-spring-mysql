# Setup Documentation

This section contains documentation for setting up and configuring the project.

## Quick Navigation

**Just getting started?** Start with [Spring Cloud Config Quick Reference](SPRING_CLOUD_CONFIG_QUICK_REFERENCE.md)
for scenario-based guidance and common issues.

---

## Available Documentation

### [Spring Cloud Config Quick Reference](SPRING_CLOUD_CONFIG_QUICK_REFERENCE.md) ⭐ START HERE

Quick overview and scenario-based guide for using Spring Cloud Config Server.

**Best for:**
- Quick answer: "Do I need a config server?"
- Step-by-step for specific deployment scenario
- Cheat sheet of environment variables
- Common issues and solutions (table format)

### [Docker Deployment Guide](DOCKER_DEPLOYMENT.md)

Guide for deploying the application using Docker Compose, including Dokploy deployments.

**Topics covered:**

- Environment variable configuration
- Docker Compose deployment
- Dokploy deployment
- Database connection troubleshooting
- Health checks and monitoring
- Persistent data management
- Spring Cloud Config Server integration

### [Spring Cloud Config Server Integration](SPRING_CLOUD_CONFIG.md)

Comprehensive guide for setting up and using Spring Cloud Config Server for centralized configuration
management across environments.

**Topics covered:**

- Configuration loading architecture and priority
- Setting up a Git-based config server
- Docker Compose integration with config server
- Creating environment-specific configurations
- Local development setup
- Dokploy deployment with config server
- Dynamic configuration updates (refresh without restart)
- Authentication and security
- Troubleshooting connectivity and configuration issues
- Best practices and migration strategies

**When to use Spring Cloud Config:**
- Managing configurations for multiple environments (dev, staging, production)
- Centralizing application settings in a Git repository
- Updating configurations without redeploying the application
- Enabling different configurations per deployment without code changes

### [Database Connection Resilience](CONNECTION_RESILIENCE.md)

Technical guide explaining how the application handles database connection failures and ensures reliable
startup.

**Topics covered:**

- HikariCP connection pool configuration
- MySQL JDBC retry parameters
- Docker health check integration
- Testing connection resilience
- Environment-specific tuning
- Troubleshooting connection issues

### [GitHub Packages Authentication Setup](GITHUB_PACKAGES_SETUP.md)

Complete guide for authenticating with GitHub Packages to access Maven dependencies.

**Topics covered:**

- GitHub Actions workflow authentication
- Local development setup with Personal Access Tokens
- Troubleshooting authentication issues
- Security best practices

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
3. (Optional) Configure Spring Cloud Config Server settings if centralized config management is needed
4. Run `docker compose up -d`
5. Access the API at `http://localhost:8080/actuator/health`

See [Docker Deployment Guide](DOCKER_DEPLOYMENT.md) for detailed instructions.

### Cloud Server Configuration (Optional)

To add centralized configuration management:

1. Set up a Git repository with your configuration files
2. Deploy or use an existing Spring Cloud Config Server
3. Set environment variables:
   - `SPRING_CLOUD_CONFIG_URI=http://config-server:8888`
   - `SPRING_APPLICATION_NAME=starter_api`
   - `SPRING_PROFILES_ACTIVE=dev` (or your target profile)
4. Restart the application

See [Spring Cloud Config Server Integration](SPRING_CLOUD_CONFIG.md) for complete setup instructions.

---

## Configuration Priority

The application loads configuration in the following order (lowest to highest priority):

1. **Defaults** in `application.yml`
2. **Spring Cloud Config Server** (if configured)
3. **Profile-specific properties** (`application-{profile}.yml`)
4. **Environment variables** (highest priority)

This means environment variables always override all other configuration sources.
