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

3. Check the status:

   ```bash
   docker compose ps
   ```

4. View logs:

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
curl -f http://localhost:8080/actuator/health
```

The API container will wait for the database to be healthy before starting, ensuring proper startup order.

## Connection Resilience

### Connection Pool Configuration

The application uses HikariCP connection pool with the following settings configured in `application.yml`:

- **Connection Timeout**: 60 seconds - Maximum time to wait for a connection from the pool
- **Maximum Pool Size**: 10 connections
- **Minimum Idle**: 2 connections - Minimum number of idle connections maintained
- **Idle Timeout**: 5 minutes - Maximum time a connection can remain idle
- **Max Lifetime**: 10 minutes - Maximum lifetime of a connection in the pool
- **Connection Test Query**: `SELECT 1` - Validates connections before use

### Connection Retry Parameters

The MySQL JDBC URL includes retry parameters to handle transient connection failures:

- `autoReconnect=true` - Automatically reconnect on connection loss
- `failOverReadOnly=false` - Don't switch to read-only mode on failover
- `maxReconnects=10` - Maximum number of reconnection attempts
- `initialTimeout=30` - Initial timeout (in seconds) for reconnection attempts

These settings help the application gracefully handle:

- MySQL container starting slower than expected
- Temporary network issues
- Database restarts or maintenance windows

## Troubleshooting

### Communications Link Failure (SQLState: 08S01)

**Symptom**: API container logs show `HHH000247: ErrorCode: 0, SQLState: 08S01 - Communications link failure`

**Common Causes**:

1. **MySQL not fully initialized**: Even though the health check passes, MySQL may still be initializing databases
2. **Network timing issues**: The API tries to connect before MySQL is ready to accept connections
3. **Missing environment variables**: Required MySQL connection parameters not set

**Solutions**:

1. **Wait and retry**: The connection pool will automatically retry with the configured parameters
2. **Check MySQL logs**:

   ```bash
   docker compose logs starterdb
   ```

   Look for messages indicating MySQL is fully initialized

3. **Verify environment variables**:

   ```bash
   docker compose config
   ```

   Ensure all `MYSQL_*` variables are set correctly

4. **Increase health check intervals**: If MySQL is consistently slow to start, adjust in `docker-compose.yml`:

   ```yaml
   starterdb:
     healthcheck:
       interval: 15s  # Increase from 10s
       timeout: 10s   # Increase from 5s
       retries: 10    # Increase from 5
   ```

### API Cannot Connect to Database

**Symptom**: API container logs show connection errors to MySQL

**Solution**: Verify the following:

1. Database hostname is `starterdb` in `SPRING_DATASOURCE_URL`
2. Database credentials match between:
   - `MYSQL_USER` / `MYSQL_PASSWORD` (database container)
   - `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD` (API container)
3. Database container is healthy before API starts
4. Connection URL includes all required parameters (see `.env.example`)

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
