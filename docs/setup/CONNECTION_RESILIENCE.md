# Database Connection Resilience

This document explains the database connection resilience features implemented to prevent
"Communications link failure" errors during Docker deployments.

## Problem Statement

When deploying with Docker Compose (especially on platforms like Dokploy), the Spring Boot application
may attempt to connect to MySQL before the database is fully ready to accept connections, even when
the health check reports the database as healthy.

This results in the following error:

```text
HHH000247: ErrorCode: 0, SQLState: 08S01
Communications link failure
```

## Root Cause

The issue occurs because:

1. **MySQL Initialization Timing**: MySQL's health check (`mysqladmin ping`) may pass before MySQL
   completes all initialization tasks (creating databases, setting up users, etc.)
2. **Network Timing**: In containerized environments, there can be brief network setup delays
3. **Single Attempt Failure**: Without retry logic, a single failed connection attempt causes the
   application to fail startup

## Solution

The fix implements a multi-layered approach to handle connection failures gracefully:

### 1. HikariCP Connection Pool Configuration

HikariCP is the default connection pool in Spring Boot. We've configured it in `application.yml`
with resilient settings:

```yaml
spring:
  datasource:
    hikari:
      connection-timeout: 60000        # 60 seconds to establish connection
      maximum-pool-size: 10            # Max 10 concurrent connections
      minimum-idle: 2                  # Keep 2 connections ready
      idle-timeout: 300000             # 5 minutes idle before closing
      max-lifetime: 600000             # 10 minutes max connection lifetime
      connection-test-query: SELECT 1  # Validate connections
```

**Key Benefits**:

- **Long Connection Timeout**: 60 seconds allows MySQL time to fully initialize
- **Connection Validation**: Tests connections before use to detect stale connections
- **Pool Management**: Maintains healthy connections and recycles them periodically

### 2. MySQL JDBC Connection Parameters

The JDBC URL in `docker-compose.yml` and `.env.example` includes retry parameters:

```text
jdbc:mysql://starterdb:3306/database?
  createDatabaseIfNotExist=true
  &useSSL=false
  &allowPublicKeyRetrieval=true
  &autoReconnect=true
  &failOverReadOnly=false
  &maxReconnects=10
  &initialTimeout=30
```

**Parameter Explanations**:

- `autoReconnect=true`: Automatically attempt to reconnect on connection loss
- `failOverReadOnly=false`: Don't switch to read-only mode during reconnection
- `maxReconnects=10`: Retry up to 10 times before failing
- `initialTimeout=30`: Initial timeout (in seconds) before first reconnection attempt (may increase
  exponentially on subsequent attempts)

### 3. Docker Compose Health Checks

The `docker-compose.yml` uses health checks to ensure proper startup order:

```yaml
api:
  depends_on:
    starterdb:
      condition: service_healthy
```

```yaml
starterdb:
  healthcheck:
    test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
    interval: 10s
    timeout: 5s
    retries: 5
```

**How It Works**:

1. MySQL container starts and runs health checks every 10 seconds
2. After 5 successful pings (50 seconds), MySQL is marked "healthy"
3. Only then does the API container start
4. Even if there's a timing issue, the connection pool and JDBC retry logic handle it

## Testing the Fix

### Local Testing with Docker Compose

1. Create a `.env` file from `.env.example`:

   ```bash
   cp .env.example .env
   ```

2. Start the services:

   ```bash
   docker compose up -d
   ```

3. Watch the logs:

   ```bash
   docker compose logs -f api
   ```

4. You should see the application start successfully without connection errors

### Simulating Connection Issues

To test the resilience, you can simulate a slow MySQL startup:

1. Start MySQL first:

   ```bash
   docker compose up -d starterdb
   ```

2. Wait a few seconds, then start the API:

   ```bash
   docker compose up -d api
   ```

3. The API should still connect successfully despite the delayed start

## Configuration for Different Environments

### Development (Fast Startup)

Use the default settings from `.env.example`. These provide good resilience without excessive delays.

### Production (High Availability)

For production environments, consider:

1. **Increased Retries**: Increase `maxReconnects` to 20 or more
2. **Longer Timeouts**: Increase `initialTimeout` to 60 seconds
3. **Larger Connection Pool**: Increase `maximum-pool-size` based on load
4. **Health Check Tuning**: Increase intervals and retries for MySQL health check

Example production configuration in `.env`:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://starterdb:3306/starterdb?\
createDatabaseIfNotExist=true&\
useSSL=false&\
allowPublicKeyRetrieval=true&\
autoReconnect=true&\
failOverReadOnly=false&\
maxReconnects=20&\
initialTimeout=60
```

**Note**: For production environments with SSL requirements, set `useSSL=true` and configure MySQL with
SSL certificates. See [MySQL SSL Configuration](https://dev.mysql.com/doc/refman/8.0/en/using-encrypted-connections.html)
for details.

And in `application.yml` (or via environment variables):

```yaml
spring:
  datasource:
    hikari:
      connection-timeout: 90000
      maximum-pool-size: 20
      minimum-idle: 5
```

### Dokploy Deployment

For Dokploy deployments, the default configuration works well. Ensure:

1. All `MYSQL_*` environment variables are set in Dokploy project settings
2. The `SPRING_DATASOURCE_URL` matches the format in `.env.example`
3. Monitor the first deployment to ensure the database initializes properly

## Monitoring Connection Health

### Application Logs

The application logs connection pool statistics at startup. Look for:

```text
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```

### Actuator Health Endpoint

The `/actuator/health` endpoint includes database connectivity status:

```bash
curl http://localhost:8080/actuator/health
```

Expected response:

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "SELECT 1"
      }
    }
  }
}
```

## Troubleshooting

### Connections Still Failing

If you still see connection failures:

1. **Check MySQL Logs**:

   ```bash
   docker compose logs starterdb | grep -i error
   ```

2. **Verify Environment Variables**:

   ```bash
   docker compose config | grep -E "MYSQL|DATASOURCE"
   ```

3. **Test Connection Manually**:

   ```bash
   docker compose exec starterdb mysql -u${MYSQL_USER} -p${MYSQL_PASSWORD} ${MYSQL_DATABASE}
   ```

4. **Increase Timeouts**: Try doubling the `connection-timeout` and `initialTimeout` values

### Connection Pool Exhaustion

If you see "Connection pool exhausted" errors:

1. **Increase Pool Size**: Set `maximum-pool-size` to a higher value
2. **Check for Leaks**: Ensure all database transactions are properly closed
3. **Monitor Active Connections**: Use the actuator metrics endpoint to track pool usage

### Slow Query Performance

If connections are established but queries are slow:

1. **Optimize Queries**: Review and optimize SQL queries
2. **Add Database Indexes**: Ensure proper indexing on frequently queried columns
3. **Increase Database Resources**: Allocate more memory to MySQL container

## Best Practices

1. **Always Use Health Checks**: Ensure `depends_on` with health check conditions in Docker Compose
2. **Monitor Connection Metrics**: Use Spring Boot Actuator to track connection pool health
3. **Set Appropriate Timeouts**: Balance between resilience and fast failure for non-recoverable errors
4. **Test Deployment Scenarios**: Test your deployment in conditions similar to production
5. **Document Custom Settings**: If you change default values, document why in your `.env` file

## References

- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP#configuration-knobs-baby)
- [MySQL JDBC Connection Parameters](https://dev.mysql.com/doc/connector-j/en/connector-j-reference-configuration-properties.html)
- [Spring Boot Database Initialization](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization)
- [Docker Compose Health Checks](https://docs.docker.com/compose/compose-file/compose-file-v3/#healthcheck)
