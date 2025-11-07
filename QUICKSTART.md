# Quickstart Guide - CI/CD

## 🚀 First Steps

### 1. Initial Configuration (one-time)

#### A. Configure Maven locally for GitHub Packages

1. Create a Personal Access Token (PAT) on GitHub:

* <https://github.com/settings/tokens>
* "Generate new token (classic)"
* Name: `Maven GitHub Packages`
* Permissions: `read:packages` (add `write:packages` if publishing)
* Generate and copy the token

1. Configure Maven:

  ```powershell
   # Windows
   copy settings.xml.example %USERPROFILE%\.m2\settings.xml
   notepad %USERPROFILE%\.m2\settings.xml

   # macOS/Linux
   cp settings.xml.example ~/.m2/settings.xml
   nano ~/.m2/settings.xml
  ```

1. Replace placeholders:

* `VOTRE_USERNAME_GITHUB` → your GitHub username
* `VOTRE_PERSONAL_ACCESS_TOKEN` → the generated PAT

#### B. Verify setup

```powershell
./mvnw.cmd clean compile
./mvnw.cmd test
./mvnw.cmd package
```

### 2. Automatic Workflow

Triggered on push to `main` or `develop`, PRs, tags `v*.*.*`, or manual dispatch.

#### Push example

```powershell
git add .
git commit -m "feat: new feature"
git push origin develop
```

Result:

* ✅ Build & tests
* 🐳 Docker image tagged `develop`

#### Pull Request

```powershell
git checkout -b feature/my-feature
git add .
git commit -m "feat: add my feature"
git push origin feature/my-feature
# Open PR in GitHub
```

Result: build & tests only.

#### Release via version bump script

```powershell
./cicd.ps1 release patch    # 0.0.1 -> 0.0.2
./cicd.ps1 release minor    # 0.0.1 -> 0.1.0
./cicd.ps1 release major    # 0.0.1 -> 1.0.0
```

Produces:

* Version update in `pom.xml`
* Commit + tag pushed
* Build, test, release artifacts
* Multi-tag Docker images (`v1.0.0`, `1.0`, `1`, `latest` if main)
* Trivy security scan

### 3. Manual Trigger

GitHub → Actions → CI/CD Pipeline → Run workflow (optionally tick Release).

## 📦 Artifacts

Download JAR from a release:

```powershell
curl -L https://github.com/tiogars/starter-api-spring-mysql/releases/download/v1.0.0/starter-1.0.0.jar -o starter.jar
java -jar starter.jar
```

Use Docker image:

```powershell
echo YOUR_PAT | docker login ghcr.io -u YOUR_USERNAME --password-stdin
docker pull ghcr.io/tiogars/starter-api-spring-mysql:latest
docker-compose up -d
```

Manual run:

```powershell
docker run -d -p 8080:8080 ^
  -e SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/starterdb ^
  -e SPRING_DATASOURCE_USERNAME=root ^
  -e SPRING_DATASOURCE_PASSWORD=password ^
  --name starter-api ^
  ghcr.io/tiogars/starter-api-spring-mysql:latest
```

## 🔍 Workflow Status

Check Actions tab or use API:

```powershell
curl https://api.github.com/repos/tiogars/starter-api-spring-mysql/actions/runs
```

## 🐛 Troubleshooting

Missing GitHub package dependency:

* Ensure PAT has `read:packages`
* Verify `~/.m2/settings.xml`
* Check package exists in GitHub Packages

Docker permission denied:
 
```powershell
sudo usermod -aG docker $USER && newgrp docker
```

Image not starting:

```powershell
docker logs starter-api
docker exec -it starter-api env | findstr SPRING
```

## 📊 Monitoring

```powershell
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/info
```

Swagger UI: <http://localhost:8080/swagger-ui/index.html>

## 🔐 Security

Update dependencies:

```powershell
./mvnw.cmd versions:display-dependency-updates
./mvnw.cmd versions:use-latest-versions
```

## 📝 Commit Conventions (Conventional Commits)

Examples:

```powershell
git commit -m "feat: add user authentication"
git commit -m "fix: resolve database connection issue"
git commit -m "docs: update API documentation"
```

## ✅ Pre-release Checklist

* Tests pass locally
* `pom.xml` version updated (if releasing)
* Docs reflect changes
* DB changes validated

Full verify:

```powershell
./mvnw.cmd clean verify
docker-compose up --build
docker-compose down
```

Tag & push:

```powershell
git tag -a v1.0.0 -m "Release 1.0.0"
git push origin v1.0.0
```

## 📞 Support

* Issues: GitHub Issues page
* Actions docs: <https://docs.github.com/actions>
* Spring Boot docs: <https://docs.spring.io/spring-boot/docs/current/reference/html/>
