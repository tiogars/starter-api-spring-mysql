# Spring Cloud Config Quick Reference

## Quick Summary

The application **optionally** uses Spring Cloud Config Server for centralized configuration. If not configured, it falls back to environment variables and local properties files.

## When to Use Spring Cloud Config

✅ **Use it** when:
- Managing multiple environments (dev, staging, prod)
- Want to change configs without redeploying
- Multiple applications share common configurations
- Need centralized audit trail of configuration changes

❌ **Skip it** when:
- Simple single-environment deployment
- Deployment already uses secrets management (Kubernetes, HashiCorp Vault)
- Configuration rarely changes
- Team is small and coordination is simple

---

## Configuration Loading Priority

(Highest to lowest - environment variables win)

```
4. Environment Variables          ← Highest priority
3. Profile-specific properties    (application-{profile}.yml)
2. Spring Cloud Config Server     (if available)
1. Default application.yml        ← Lowest priority
```

---

## Environment Variables Cheat Sheet

### Required for Database

```bash
MYSQL_ROOT_PASSWORD=password
MYSQL_DATABASE=starterdb
MYSQL_USER=starter
MYSQL_PASSWORD=starter123
```

### Optional for Config Server

```bash
SPRING_CLOUD_CONFIG_URI=http://config-server:8888        # Where is config server?
SPRING_CLOUD_CONFIG_USERNAME=username                     # Needed if server requires auth
SPRING_CLOUD_CONFIG_PASSWORD=password                     # Needed if server requires auth
SPRING_CLOUD_CONFIG_LABEL=main                            # Git branch (defaults to main)
SPRING_APPLICATION_NAME=starter_api                       # App name (matches config files)
SPRING_PROFILES_ACTIVE=dev                                # Which profile to load
```

---

## Deployment Scenarios

### Scenario 1: Docker Compose Without Config Server (Simplest)

```bash
# 1. Create .env file
cp .env.example .env

# 2. Set database credentials only
MYSQL_ROOT_PASSWORD=password
MYSQL_DATABASE=starterdb
MYSQL_USER=starter
MYSQL_PASSWORD=starter123

# 3. Deploy
docker compose up -d

# Application uses env vars for all config
```

**Files to read**: [Docker Deployment Guide](DOCKER_DEPLOYMENT.md)

---

### Scenario 2: Docker Compose With Config Server

```bash
# 1. Set up Git repository with config files
git clone https://github.com/youruser/my-config-repo.git
# Add: starter_api.yml, starter_api-dev.yml, starter_api-prod.yml

# 2. Deploy config server (or use existing one)
# Docker: hyness/spring-cloud-config-server:latest
# URL: http://config-server:8888

# 3. Set environment variables
SPRING_CLOUD_CONFIG_URI=http://config-server:8888
SPRING_CLOUD_CONFIG_LABEL=main

# 4. Deploy application
docker compose up -d
```

**Files to read**: [Spring Cloud Config Server Integration](SPRING_CLOUD_CONFIG.md)

---

### Scenario 3: Local Development

```bash
# 1. Start config server locally
docker run -d -p 8888:8888 \
  -e SPRING_CLOUD_CONFIG_SERVER_GIT_URI=file:///path/to/config-repo \
  hyness/spring-cloud-config-server:latest

# 2. Set environment variables
export SPRING_CLOUD_CONFIG_URI=http://localhost:8888
export SPRING_APPLICATION_NAME=starter_api
export SPRING_PROFILES_ACTIVE=dev

# 3. Run application
mvn spring-boot:run
```

**Files to read**: [Spring Cloud Config Server Integration](SPRING_CLOUD_CONFIG.md) - "Local Development Setup"

---

### Scenario 4: Dokploy Deployment

```
1. In Dokploy UI:
   Project Settings → Environment Variables
   
2. Add required variables:
   SPRING_CLOUD_CONFIG_URI=https://your-config-server.com
   SPRING_APPLICATION_NAME=starter_api
   SPRING_PROFILES_ACTIVE=prod

3. Dokploy will use docker-compose.yml automatically
```

**Files to read**: [Spring Cloud Config Server Integration](SPRING_CLOUD_CONFIG.md) - "Dokploy Deployment"

---

## Configuration File Naming

In your config server Git repository:

```
starter_api.yml              # Always loaded (default config)
starter_api-dev.yml          # Loaded when SPRING_PROFILES_ACTIVE=dev
starter_api-staging.yml      # Loaded when SPRING_PROFILES_ACTIVE=staging
starter_api-prod.yml         # Loaded when SPRING_PROFILES_ACTIVE=prod
```

**Important**: `SPRING_APPLICATION_NAME` must match the filename prefix!

---

## Example Configuration File

```yaml
# starter_api-dev.yml (stored in config server Git repo)
spring:
  datasource:
    url: jdbc:mysql://db-dev:3306/mydb_dev
    username: dev_user
    password: dev_pass
  jpa:
    hibernate:
      ddl-auto: create-drop  # Recreate schema
    show-sql: true

logging:
  level:
    root: DEBUG              # Show debug logs

server:
  port: 8080
```

---

## Common Issues

| Problem | Check | Solution |
|---------|-------|----------|
| **Config server unreachable** | Logs show: `No environment found` | 1. Is config server running? <br> 2. Is `SPRING_CLOUD_CONFIG_URI` correct? <br> 3. Can app reach config server URL? |
| **Auth failed** | Logs show: `401 Unauthorized` | 1. Verify credentials in `SPRING_CLOUD_CONFIG_USERNAME/PASSWORD` <br> 2. Check config server has auth enabled |
| **Properties not applied** | Config loaded but properties ignored | 1. Check property names are exact (case-sensitive) <br> 2. Verify profile name matches: `SPRING_PROFILES_ACTIVE` <br> 3. Are env vars overriding (they take priority)? |
| **File not found** | Logs show: `404 Not Found` | 1. Does file exist: `starter_api.yml`? <br> 2. Does profile file exist: `starter_api-{profile}.yml`? <br> 3. Is `SPRING_CLOUD_CONFIG_LABEL` pointing to correct branch? |

---

## Testing Your Setup

### Test 1: Config Server Connection

```bash
# From your container or dev machine
curl http://config-server:8888/starter_api/dev

# Should return JSON like:
# {
#   "name": "starter_api",
#   "profiles": ["dev"],
#   "propertySources": [...]
# }
```

### Test 2: Application Startup

```bash
# Check application logs
docker compose logs -f api | grep -i "config\|loaded\|properties"

# Look for messages like:
# "Successfully acquired a new config from config server"
# or fallback message if server unavailable:
# "No environment found for application 'starter_api' with profiles [dev]"
```

### Test 3: Verify Properties Loaded

```bash
# Access application actuator
curl http://localhost:8080/actuator/env | grep -i datasource

# Should show your config values
```

---

## Dynamic Reload (Without Restart)

Update configs in Git and reload:

```bash
# 1. Commit changes in config server Git repo
git add .
git commit -m "Update prod config"
git push

# 2. Trigger reload
curl -X POST http://localhost:8080/actuator/refresh

# Beans marked with @RefreshScope will reload
```

---

## Documentation Hierarchy

```
📄 THIS FILE (Overview & Quick Reference)
    ↓
📘 docs/setup/SPRING_CLOUD_CONFIG.md (Detailed Guide)
    - Complete setup instructions
    - All deployment scenarios
    - Troubleshooting (5+ scenarios)
    - Security best practices
    - Migration strategies
    ↓
📘 docs/setup/DOCKER_DEPLOYMENT.md (Docker-specific)
    - Docker Compose details
    - Dokploy deployment
    - Connection resilience
    ↓
📘 src/main/resources/application.yml (Code Reference)
    - Actual config implementation
    - All environment variables explained
```

---

## Links to Detailed Docs

- **Full Setup Guide**: [Spring Cloud Config Server Integration](SPRING_CLOUD_CONFIG.md)
- **Docker Deployment**: [Docker Deployment Guide](DOCKER_DEPLOYMENT.md)
- **All Setup Docs**: [Setup Documentation Index](index.md)
- **Application Config**: [application.yml](../../src/main/resources/application.yml)

---

## Need Help?

1. **Before you restart**: Check the logs for spring config messages
2. **Can't find files**: Look in `/docs/setup/SPRING_CLOUD_CONFIG.md` troubleshooting section
3. **Docker issues**: See troubleshooting in [DOCKER_DEPLOYMENT.md](DOCKER_DEPLOYMENT.md)
4. **Implementation questions**: Check [application.yml](../../src/main/resources/application.yml)

