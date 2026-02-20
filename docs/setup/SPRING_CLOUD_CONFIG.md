# Spring Cloud Config Server Integration

This document explains how to configure and use Spring Cloud Config Server with the application,
providing centralized configuration management across environments.

## Overview

The application is configured to optionally use **Spring Cloud Config Server** for centralized
configuration management. This allows you to:

- Manage configuration files in a Git repository
- Centralize settings for multiple application instances
- Update configurations without redeploying the application
- Maintain environment-specific configurations (dev, staging, production)

The config server integration is **optional** - if not configured, the application uses direct
environment variables and properties files.

## Architecture

### Configuration Loading Order

The application loads configuration in the following priority order:

1. **Spring Cloud Config Server** (if configured and available)
   - Fetches properties from the config server
   - Uses the `SPRING_CLOUD_CONFIG_URI` endpoint
2. **Local Properties File** (`application-{profile}.yml`)
   - Profile-specific properties (e.g., `application-dev.yml`, `application-prod.yml`)
3. **Environment Variables**
   - System environment variables override all local properties
4. **Application Properties** (`application.yml`)
   - Default fallback values

### Configuration File Structure in Config Server

In your config server Git repository, organize files like:

```
/my-config-repo
├── starter_api.yml                    # Default config for all profiles
├── starter_api-dev.yml                # Development profile
├── starter_api-staging.yml            # Staging profile
├── starter_api-prod.yml               # Production profile
└── config-server-config.yml           # Config server's own properties
```

The application name and profile are used as follows:

- **Application name**: Derived from `SPRING_APPLICATION_NAME` (default: `starter_api`)
- **Active profile**: Set via `SPRING_PROFILES_ACTIVE` (default: `dev`)
- **Config branch**: Set via `SPRING_CLOUD_CONFIG_LABEL` (default: `main`)

## Setup Instructions

### 1. Prepare Your Config Server

#### Option A: Use a Git-based Config Server (Recommended)

Create a Git repository to store your configurations:

```bash
# Create a config repository
mkdir my-config-repo
cd my-config-repo
git init

# Create application configuration files
echo "spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: root
server:
  port: 8080" > starter_api.yml

# Create profile-specific configs
echo "spring:
  datasource:
    url: jdbc:mysql://db-dev:3306/mydb_dev
    username: dev_user
    password: dev_pass" > starter_api-dev.yml

# Commit and push
git add .
git commit -m "Initial config"
git push origin main
```

Then, start the config server pointing to your repository:

```bash
# Using Spring Cloud Config Server Docker image
docker run -d \
  -p 8888:8888 \
  -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=https://github.com/youruser/my-config-repo.git \
  hyness/spring-cloud-config-server:latest
```

Or if you prefer a local setup:

```bash
# Clone the Spring Cloud Config Server project and run it locally
git clone https://github.com/spring-cloud-samples/config-repo.git
cd spring-cloud-config-server
./mvnw spring-boot:run \
  --args='--spring.cloud.config.server.git.uri=file://$(pwd)/../config-repo'
```

#### Option B: Use Docker Compose with Config Server

Add the config server service to your `docker-compose.yml`:

```yaml
services:
  config-server:
    image: hyness/spring-cloud-config-server:latest
    container_name: config-server
    ports:
      - "8888:8888"
    environment:
      SPRING_CLOUD_CONFIG_SERVER_GIT_URI: https://github.com/youruser/my-config-repo.git
      SPRING_CLOUD_CONFIG_SERVER_GIT_DEFAULT_LABEL: main
    networks:
      - starter_api_network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8888/health"]
      interval: 10s
      timeout: 5s
      retries: 5

  api:
    depends_on:
      config-server:
        condition: service_healthy
    # ... rest of api configuration
```

### 2. Configure Application Connection

#### For Docker Compose Deployment

Set these environment variables in your `.env` file:

```bash
# Spring Cloud Config Server settings
SPRING_CLOUD_CONFIG_URI=http://config-server:8888
SPRING_CLOUD_CONFIG_USERNAME=                    # Optional: if config server requires auth
SPRING_CLOUD_CONFIG_PASSWORD=                    # Optional: if config server requires auth
SPRING_CLOUD_CONFIG_LABEL=main                   # Optional: Git branch (default: main)

# Application settings
SPRING_APPLICATION_NAME=starter_api              # Must match config file names
SPRING_PROFILES_ACTIVE=dev                       # Which profile to load
```

#### For Local Development

Set environment variables before running:

```bash
export SPRING_CLOUD_CONFIG_URI=http://localhost:8888
export SPRING_APPLICATION_NAME=starter_api
export SPRING_PROFILES_ACTIVE=dev
export SPRING_PROFILES_INCLUDE=

mvn spring-boot:run
```

Or use a `.env` file with your IDE:

```bash
# .env.local (add to .gitignore)
SPRING_CLOUD_CONFIG_URI=http://localhost:8888
SPRING_APPLICATION_NAME=starter_api
SPRING_PROFILES_ACTIVE=dev
```

#### For Dokploy Deployment

In your Dokploy project settings, add these environment variables:

- `SPRING_CLOUD_CONFIG_URI`: URL of your config server (e.g., `https://config.example.com`)
- `SPRING_CLOUD_CONFIG_USERNAME`: Username (if authentication required)
- `SPRING_CLOUD_CONFIG_PASSWORD`: Password (if authentication required)
- `SPRING_CLOUD_CONFIG_LABEL`: Git branch/label (defaults to `main`)
- `SPRING_APPLICATION_NAME`: Application identifier matching config files
- `SPRING_PROFILES_ACTIVE`: Profile to load (e.g., `prod`)

### 3. Application Configuration File

The application's `src/main/resources/application.yml` includes the config server settings:

```yaml
spring:
  cloud:
    config:
      name: ${SPRING_APPLICATION_NAME:starter_api}
      label: ${SPRING_CLOUD_CONFIG_LABEL:main}
      uri: ${SPRING_CLOUD_CONFIG_URI:http://localhost:8888}
      username: ${SPRING_CLOUD_CONFIG_USERNAME}
      password: ${SPRING_CLOUD_CONFIG_PASSWORD}
  config:
    import: optional:configserver:${SPRING_CLOUD_CONFIG_URI:http://localhost:8888}
```

**Key Settings**:

- `name`: Application name for config file matching (e.g., looks for `starter_api.yml`)
- `label`: Git branch containing the configuration (default: `main`)
- `uri`: Config server URL
- `username`/`password`: Credentials if config server requires authentication
- `import`: Imports config with `optional:` prefix, meaning startup won't fail if server is unavailable

## Configuration Management

### Creating Environment-Specific Configs

Store each profile-specific configuration in the config server Git repository:

```yaml
# starter_api-dev.yml (Development)
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true

logging:
  level:
    root: DEBUG

# starter_api-prod.yml (Production)
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: false

logging:
  level:
    root: WARN
```

### Overriding with Environment Variables

Environment variables always take precedence over config server files:

```bash
# Override specific datasource settings
SPRING_DATASOURCE_URL=jdbc:mysql://custom-host:3306/db
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=secret123

# These will override values from config server files
```

### Dynamic Configuration Updates

To reload configuration without restart:

1. Update the configuration in your Git repository
2. Commit and push changes
3. Make a POST request to the refresh endpoint:

```bash
curl -X POST http://localhost:8080/actuator/refresh

# With Spring Cloud Bus (if configured):
curl -X POST http://localhost:8080/actuator/busrefresh
```

Affected beans with `@RefreshScope` annotation will be reinitialized.

## Troubleshooting

### Config Server Connection Failures

**Symptom**: Application starts with warning about config server unreachable

```
WARN o.s.c.c.c.ConfigServicePropertySourceLocator : No environment found for
application 'starter_api' with profiles [dev]
```

**Solution**: This is expected when using `optional:configserver:`. The application will:
- Fall back to local `application.yml` properties
- Use environment variables
- Continue startup successfully

If you want the application to fail on config server unavailability, change `optional:` to
`configserver:` in `application.yml`.

### Config Server Authentication Failures

**Symptom**: Application fails to connect with 401/403 errors

```
ys.c.ConfigServerProvider : Retry with exponential backoff and jitter
Authorization header not present
```

**Solution**:

1. **Verify credentials**:
   ```bash
   curl -u username:password http://config-server:8888/starter_api/dev
   ```

2. **Check environment variables** are set correctly:
   ```bash
   echo $SPRING_CLOUD_CONFIG_USERNAME
   echo $SPRING_CLOUD_CONFIG_PASSWORD
   ```

3. **If using HTTP Basic Auth**, ensure the config server has authentication configured:
   ```yaml
   # In config server's application.yml
   spring:
     security:
       user:
         name: admin
         password: secret
   ```

### Config File Not Found

**Symptom**: Config server returns 404 for application configuration

```
HTTP/1.1 404
{"status":"NOT_FOUND","message":"..."}
```

**Solution**:

1. Verify file exists in config repository:
   ```bash
   # Should exist in config server Git repo
   ls -la config-repo/starter_api.yml
   ls -la config-repo/starter_api-dev.yml
   ```

2. Check `SPRING_APPLICATION_NAME` matches the filename:
   ```bash
   # If SPRING_APPLICATION_NAME=starter_api, files must be:
   starter_api.yml
   starter_api-dev.yml
   ```

3. Verify `SPRING_CLOUD_CONFIG_LABEL` points to correct Git branch:
   ```bash
   # Check available branches in config server repository
   git -C config-repo branch -a
   ```

### Properties Not Overriding

**Symptom**: Config server properties don't seem to take effect

**Solution**:

1. **Check import order** in application.yml - `spring.config.import` must come before
   `spring.cloud.config`
2. **Verify property names** match exactly (property names are case-sensitive in YAML)
3. **Check profile order**:
   ```bash
   # Active profiles in order (first match wins)
   SPRING_PROFILES_ACTIVE=dev,custom

   # Files loaded in this order:
   # 1. starter_api.yml
   # 2. starter_api-dev.yml
   # 3. starter_api-custom.yml
   # 4. Environment variables (highest priority)
   ```

### Testing Config Server Connection

Verify the connection is working:

```bash
# Test config server directly
curl http://localhost:8888/starter_api/dev

# Should return something like:
# {
#   "name": "starter_api",
#   "profiles": ["dev"],
#   "label": "main",
#   "version": "abc123def",
#   "propertySources": [
#     {
#       "name": "...",
#       "source": { ... }
#     }
#   ]
# }
```

Check application logs for config loading details:

```bash
# View application logs
docker compose logs -f api | grep -i "config\|cloud"

# Enable debug logging in config server requests
export SPRING_CLOUD_CONFIG_ENABLED_DEBUG=true
```

## Best Practices

### Security

1. **Use HTTPS** for config server in production:
   ```bash
   SPRING_CLOUD_CONFIG_URI=https://config.example.com:8888
   ```

2. **Protect sensitive properties** using encryption:
   ```yaml
   # In config server, encrypt sensitive values
   spring:
     cloud:
       config:
         server:
           git:
             uri: https://github.com/youruser/my-config-repo.git
             searchPaths: 'config-repo'
   ```

3. **Use config server authentication**:
   ```bash
   SPRING_CLOUD_CONFIG_USERNAME=config-user
   SPRING_CLOUD_CONFIG_PASSWORD=strong-password
   ```

4. **Rotate credentials** in `.env` files and config server regularly

### Organization

1. **Keep config repository separate** from application code
2. **Use consistent naming** for application and profile names
3. **Document** which properties each profile should contain
4. **Use feature branches** for testing new configurations

### Monitoring

1. **Enable config server health checks**:
   ```bash
   # Check if config server is responding
   curl http://config-server:8888/health
   ```

2. **Monitor startup logs** for config loading messages

3. **Set up alerts** for config server failures

## Migration from Direct Properties

If you're migrating from direct environment variables to config server:

1. **Copy current settings** into config server configuration files
2. **Test in dev environment** first with `SPRING_PROFILES_ACTIVE=dev`
3. **Verify all properties** are loaded correctly from config server
4. **Remove hardcoded defaults** from application.yml
5. **Update deployment documentation** with config server endpoints
6. **Keep fallback values** in application.yml for critical properties

## Related Documentation

- [Docker Deployment Guide](DOCKER_DEPLOYMENT.md) - Integration with Docker Compose
- [Database Connection Resilience](CONNECTION_RESILIENCE.md) - Connection pool configuration
- [Spring Cloud Config Server Documentation](https://cloud.spring.io/spring-cloud-config/reference/html/)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
