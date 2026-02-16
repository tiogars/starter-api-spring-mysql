# Docker Deployment Guide

This guide explains how to deploy the application using Docker Compose, including Dokploy deployments.

## Overview

The application uses Docker Compose to orchestrate two services:

- **api**: The Spring Boot application
- **starterdb**: MySQL 8.0 database

## Configuration

### Environment Variables

The application requires several environment variables to run. Create a `.env` file based on `.env.example`:

```bash
cp .env.example .env
```

### Required Variables for Docker Compose

The following variables are essential for the database connection:

```bash
# Database configuration
MYSQL_ROOT_PASSWORD=password
MYSQL_DATABASE=starterdb
MYSQL_USER=starter
MYSQL_PASSWORD=starter123

# Application configuration
SPRING_APPLICATION_NAME=starter-api
SPRING_PROFILES_ACTIVE=dev
```

### Database Connection

The API container connects to the MySQL database using these environment variables:

- `SPRING_DATASOURCE_URL`: Default is `jdbc:mysql://starterdb:3306/${MYSQL_DATABASE}?...`
- `SPRING_DATASOURCE_USERNAME`: Default is the value of `${MYSQL_USER}`
- `SPRING_DATASOURCE_PASSWORD`: Default is the value of `${MYSQL_PASSWORD}`

**Important**: The database hostname must be `starterdb` to match the service name in docker-compose.yml.

### Optional: Spring Cloud Config

If you want to use Spring Cloud Config Server, you can set these optional variables:

```bash
SPRING_CLOUD_CONFIG_URI=http://config-server:8888
SPRING_CLOUD_CONFIG_USERNAME=your-username
SPRING_CLOUD_CONFIG_PASSWORD=your-password
```

If these are not set, the application will use the direct datasource configuration from environment variables.

## Deployment

### Using Docker Compose

1. Ensure you have a `.env` file configured (see above)

2. Start the services:

```bash
docker compose up -d
```

1. Check the status:

```bash
docker compose ps
```

1. View logs:

```bash
docker compose logs -f api
docker compose logs -f starterdb
```

### Using Dokploy

Dokploy uses Docker Compose under the hood. To deploy with Dokploy:

1. **Configure Environment Variables**: In your Dokploy project, set all required environment variables:
   - `MYSQL_ROOT_PASSWORD`
   - `MYSQL_DATABASE`
   - `MYSQL_USER`
   - `MYSQL_PASSWORD`
   - `SPRING_APPLICATION_NAME`
   - `SPRING_PROFILES_ACTIVE`

2. **Deploy**: Dokploy will automatically use the `docker-compose.yml` file and apply your environment variables.

3. **Verify**: Check that both containers are healthy:
   - The `starterdb` container should be running and healthy
   - The `api` container should start after the database is healthy (due to `depends_on` configuration)

## Health Checks

### Database Health Check

The MySQL container includes a health check that runs every 10 seconds:

```bash
mysqladmin ping -h localhost
```

### API Health Check

The API container includes a health check that runs every 10 seconds:

```bash
curl -f http://api:8080/actuator/health
```

The API container will wait for the database to be healthy before starting, ensuring proper startup order.

## Troubleshooting

### API Cannot Connect to Database

**Symptom**: API container logs show connection errors to MySQL

**Solution**: Verify the following:

1. Database hostname is `starterdb` in `SPRING_DATASOURCE_URL`
2. Database credentials match between:
   - `MYSQL_USER` / `MYSQL_PASSWORD` (database container)
   - `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` (API container)
3. Database container is healthy before API starts

### Spring Cloud Config Not Available

**Symptom**: Application tries to connect to Spring Cloud Config Server but fails

**Solution**: Spring Cloud Config is optional. If you're not using it:

- Leave `SPRING_CLOUD_CONFIG_URI`, `SPRING_CLOUD_CONFIG_USERNAME`, and
  `SPRING_CLOUD_CONFIG_PASSWORD` unset or empty
- Ensure direct datasource configuration is provided via environment variables

### Port Conflicts

**Symptom**: Cannot start containers due to port already in use

**Solution**: The default configuration has ports commented out. If you need to expose ports locally:

Uncomment the ports section in `docker-compose.yml`:

```yaml
ports:
  - "8080:8080"
```

## Memory Limits

Both containers have memory limits configured:

- API: 512MB
- Database: 512MB

Adjust these in `docker-compose.yml` if needed based on your workload.

## Persistent Data

Database data is persisted in a Docker volume named `mysql-data`. To remove all data:

```bash
docker compose down -v
```

**Warning**: This will delete all database data!

## Related Documentation

- [GitHub Packages Setup](GITHUB_PACKAGES_SETUP.md)
- [API Documentation](../api/index.md)
- [Testing Documentation](../testing/index.md)
