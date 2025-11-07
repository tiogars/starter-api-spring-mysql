# GitHub Actions CI/CD Configuration

This repository uses GitHub Actions to automate build, tests, releases, and Docker publishing.

## 🔄 Available Workflows

### Full CI/CD Pipeline (`ci-cd.yml`)

This workflow runs on:

* Pushes to `main` and `develop`
* Tag creation `v*.*.*`
* Pull requests targeting `main` and `develop`
* Manual dispatch

#### Included jobs

1. **Build and Test**
    * Configure Java 21 and Maven
    * Fetch dependencies from GitHub Packages
    * Compile the application
    * Run tests
    * Upload artifacts

2. **Create Release** (tags only)
    * Create a GitHub Release
    * Generate an automatic changelog
    * Attach sources and built JAR

3. **Docker Build & Push**
    * Multi-platform build (amd64/arm64)
    * Push to GitHub Container Registry
    * Generate SBOM

4. **Security Scan**
    * Scan vulnerabilities with Trivy
    * Upload results to GitHub Security

## 📋 Prerequisites

### GitHub Secrets (auto-provisioned)

* `GITHUB_TOKEN`: Token automatically provided by GitHub Actions

### Local Maven configuration

To access GitHub Packages locally, create `~/.m2/settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                                             https://maven.apache.org/xsd/settings-1.0.0.xsd">
   <servers>
      <server>
         <id>github</id>
         <username>YOUR_GITHUB_USERNAME</username>
         <password>YOUR_PERSONAL_ACCESS_TOKEN</password>
      </server>
   </servers>
   <profiles>
      <profile>
         <id>github</id>
         <repositories>
            <repository>
               <id>central</id>
               <url>https://repo.maven.apache.org/maven2</url>
            </repository>
            <repository>
               <id>github</id>
               <url>https://maven.pkg.github.com/tiogars/*</url>
            </repository>
         </repositories>
      </profile>
   </profiles>
   <activeProfiles>
      <activeProfile>github</activeProfile>
   </activeProfiles>
</settings>
```

Create a Personal Access Token (PAT):

1. GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Generate with permission: `read:packages`

## 🚀 Usage

### Trigger an automatic build

```bash
# On develop or main
git add .
git commit -m "feat: new feature"
git push
```

### Create a release

```bash
# Create a version tag
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

The workflow will automatically:

1. ✅ Build and test the application
2. 📦 Create a GitHub Release with artifacts
3. 🐳 Build and push the Docker image
4. 🔒 Scan for vulnerabilities

### Manual dispatch

Go to: `Actions` → `CI/CD Pipeline` → `Run workflow`

## 🐳 Use the Docker image

### Pull from GitHub Container Registry

```bash
# Latest
docker pull ghcr.io/tiogars/starter-api-spring-mysql:latest

# Specific version
docker pull ghcr.io/tiogars/starter-api-spring-mysql:v1.0.0

# develop branch
docker pull ghcr.io/tiogars/starter-api-spring-mysql:develop
```

### Run the application

```bash
# With docker-compose (recommended)
docker-compose up -d

# Or manually
docker run -d \
   -p 8080:8080 \
   -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/starterdb \
   -e SPRING_DATASOURCE_USERNAME=root \
   -e SPRING_DATASOURCE_PASSWORD=password \
   --name starter-api \
   ghcr.io/tiogars/starter-api-spring-mysql:latest
```

## 📊 Available artifacts

After each successful build you will find:

### In GitHub Actions

* build-artifacts: built JAR (7 days)
* test-results: test reports (7 days)
* sbom: Software Bill of Materials (90 days)

### In GitHub Releases (tags)

* `starter-VERSION.jar`: compiled application
* `starter-api-VERSION-sources.zip`: source code (ZIP)
* `starter-api-VERSION-sources.tar.gz`: source code (TAR.GZ)

### In GitHub Container Registry

* Docker images with multiple tags
* Multi-architecture support (amd64, arm64)

## 🔐 Security

### Vulnerability scanning

Each Docker image is scanned with Trivy to detect:

* CRITICAL and HIGH vulnerabilities
* Results available in the Security tab

### SBOM (Software Bill of Materials)

An SBOM in CycloneDX format is generated for each build and kept for 90 days.

## 🏷️ Versioning convention

The project uses Semantic Versioning (SemVer):

* `v1.0.0`: Stable release
* `v1.0.0-RC1`: Release candidate (prerelease)
* `v1.0.0-SNAPSHOT`: Development version (prerelease)

### Automatically generated Docker tags

* `latest`: Latest version from main branch
* `v1.0.0`: Exact version tag
* `1.0`: Major.minor tag
* `1`: Major tag
* `develop`: develop branch
* `main-abc1234`: SHA of a commit on main

## 📝 Important notes

1. GitHub dependencies: `architecture-create-service` and `architecture-select-service` must be available in GitHub Packages for account `tiogars`.
2. Maven cache: handled automatically by GitHub Actions to speed up builds.
3. Multi-platform builds: images are built for AMD64 and ARM64.
4. Tests: tests must pass for the workflow to continue (Docker build may continue depending on workflow conditions).

## 🔧 Maintenance

### Update dependencies

```bash
./mvnw versions:display-dependency-updates
./mvnw versions:use-latest-releases
```

### Clean old artifacts

Artifacts are automatically removed after:

* 7 days for build-artifacts and test-results
* 90 days for SBOM

## 📚 Resources

* [GitHub Actions Documentation](https://docs.github.com/actions)
* [GitHub Packages Maven](https://docs.github.com/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)
* [GitHub Container Registry](https://docs.github.com/packages/working-with-a-github-packages-registry/working-with-the-container-registry)
* [Spring Boot with Docker](https://spring.io/guides/topicals/spring-boot-docker)
