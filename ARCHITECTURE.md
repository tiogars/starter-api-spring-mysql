# Architecture CI/CD - Diagramme et Flux

## 🏗️ Architecture globale

```
┌─────────────────────────────────────────────────────────────────────┐
│                         DÉVELOPPEUR LOCAL                            │
│                                                                       │
│  ┌──────────────┐    ┌──────────────┐    ┌──────────────┐          │
│  │   cicd.ps1   │ → │    Maven     │ → │   GitHub     │          │
│  │   (Helper)   │    │   + Java 21  │    │   Packages   │          │
│  └──────────────┘    └──────────────┘    └──────────────┘          │
│         │                    │                    │                  │
└─────────┼────────────────────┼────────────────────┼──────────────────┘
          │                    │                    │
          ▼                    ▼                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                           GIT REPOSITORY                             │
│                    github.com/tiogars/starter-api                   │
│                                                                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                │
│  │   develop   │  │    main     │  │  v*.*.*     │                │
│  │  (feature)  │  │  (stable)   │  │  (release)  │                │
│  └─────────────┘  └─────────────┘  └─────────────┘                │
│         │                │                │                          │
└─────────┼────────────────┼────────────────┼──────────────────────────┘
          │                │                │
          ▼                ▼                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        GITHUB ACTIONS                                │
│                      (Workflows Automatiques)                        │
│                                                                       │
│  ┌───────────────────────────────────────────────────────────┐      │
│  │  JOB 1: Build & Test                                      │      │
│  │  ├─ Setup Java 21                                         │      │
│  │  ├─ Configure Maven (GitHub Packages auth)               │      │
│  │  ├─ Fetch dependencies from GitHub Packages              │      │
│  │  ├─ Compile (mvn compile)                                │      │
│  │  ├─ Run tests (mvn test)                                 │      │
│  │  ├─ Package (mvn package)                                │      │
│  │  └─ Upload artifacts (JAR, test reports)                 │      │
│  └───────────────────────────────────────────────────────────┘      │
│         │                                                             │
│         ▼                                                             │
│  ┌───────────────────────────────────────────────────────────┐      │
│  │  JOB 2: Create Release (only on tags)                    │      │
│  │  ├─ Download build artifacts                             │      │
│  │  ├─ Create source archives (zip, tar.gz)                 │      │
│  │  ├─ Generate changelog from git history                  │      │
│  │  └─ Create GitHub Release with attachments               │      │
│  └───────────────────────────────────────────────────────────┘      │
│         │                                                             │
│         ▼                                                             │
│  ┌───────────────────────────────────────────────────────────┐      │
│  │  JOB 3: Docker Build & Push                               │      │
│  │  ├─ Setup Docker Buildx (multi-platform)                 │      │
│  │  ├─ Login to GitHub Container Registry                   │      │
│  │  ├─ Extract metadata (tags, labels)                      │      │
│  │  ├─ Build image (amd64 + arm64)                          │      │
│  │  ├─ Push to ghcr.io/tiogars/starter-api-spring-mysql    │      │
│  │  └─ Generate SBOM (CycloneDX)                            │      │
│  └───────────────────────────────────────────────────────────┘      │
│         │                                                             │
│         ▼                                                             │
│  ┌───────────────────────────────────────────────────────────┐      │
│  │  JOB 4: Security Scan                                     │      │
│  │  ├─ Pull Docker image                                     │      │
│  │  ├─ Run Trivy vulnerability scanner                      │      │
│  │  ├─ Generate SARIF report                                │      │
│  │  └─ Upload to GitHub Security                            │      │
│  └───────────────────────────────────────────────────────────┘      │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
          │                │                │
          ▼                ▼                ▼
┌─────────────────────────────────────────────────────────────────────┐
│                           ARTIFACTS & OUTPUTS                        │
│                                                                       │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐    │
│  │ GitHub Releases │  │  Docker Images  │  │ Security Reports│    │
│  │                 │  │                 │  │                 │    │
│  │ • JAR files     │  │ ghcr.io tags:   │  │ • Trivy scans   │    │
│  │ • Sources (zip) │  │ • latest        │  │ • SARIF format  │    │
│  │ • Changelogs    │  │ • v1.0.0        │  │ • GitHub Sec.   │    │
│  │                 │  │ • 1.0           │  │                 │    │
│  └─────────────────┘  │ • 1             │  └─────────────────┘    │
│                       │ • develop       │                          │
│                       │ • main-sha      │                          │
│                       └─────────────────┘                          │
└─────────────────────────────────────────────────────────────────────┘
```

## 🔄 Flux de travail détaillé

### 1️⃣ Push sur branche (develop/main)

```
Developer                GitHub Actions                 GitHub Registry
    │                           │                              │
    │ git push origin develop   │                              │
    ├──────────────────────────>│                              │
    │                           │                              │
    │                           │ ① Build & Test               │
    │                           ├─────────────┐                │
    │                           │             │                │
    │                           │<────────────┘                │
    │                           │                              │
    │                           │ ② Docker Build               │
    │                           ├─────────────────────────────>│
    │                           │         Push image           │
    │                           │     (tag: develop)           │
    │                           │                              │
    │                           │ ③ Security Scan              │
    │                           ├─────────────┐                │
    │                           │             │                │
    │<──────── Notification ────┤<────────────┘                │
    │      (success/fail)       │                              │
```

### 2️⃣ Pull Request

```
Developer                GitHub Actions                 Status Check
    │                           │                              │
    │ Create PR                 │                              │
    ├──────────────────────────>│                              │
    │                           │                              │
    │                           │ ① Build & Test               │
    │                           ├─────────────────────────────>│
    │                           │    Report status             │
    │                           │                              │
    │<──────── Status ──────────┤                              │
    │    ✅ All checks passed   │                              │
    │    ❌ Tests failed        │                              │
    │                           │                              │
    │ (NO Docker build on PR)   │                              │
```

### 3️⃣ Release avec Tag

```
Developer          GitHub Actions      GitHub Releases    Docker Registry    Security
    │                    │                    │                  │              │
    │ git tag v1.0.0     │                    │                  │              │
    │ git push --tags    │                    │                  │              │
    ├───────────────────>│                    │                  │              │
    │                    │                    │                  │              │
    │                    │ ① Build & Test     │                  │              │
    │                    ├──────────┐         │                  │              │
    │                    │          │         │                  │              │
    │                    │<─────────┘         │                  │              │
    │                    │                    │                  │              │
    │                    │ ② Create Release   │                  │              │
    │                    ├───────────────────>│                  │              │
    │                    │   • JAR            │                  │              │
    │                    │   • Sources        │                  │              │
    │                    │   • Changelog      │                  │              │
    │                    │                    │                  │              │
    │                    │ ③ Docker Multi-Tag │                  │              │
    │                    ├───────────────────────────────────────>│              │
    │                    │   • v1.0.0         │                  │              │
    │                    │   • 1.0            │                  │              │
    │                    │   • 1              │                  │              │
    │                    │   • latest         │                  │              │
    │                    │                    │                  │              │
    │                    │ ④ Security Scan    │                  │              │
    │                    ├─────────────────────────────────────────────────────>│
    │                    │   Trivy scan       │                  │              │
    │                    │                    │                  │              │
    │<──── Notifications ┤                    │                  │              │
    │   (Email/GitHub)   │                    │                  │              │
```

## 📊 Matrice de déclenchement

| Événement | Build & Test | Release | Docker Push | Security Scan |
|-----------|--------------|---------|-------------|---------------|
| Push (develop) | ✅ | ❌ | ✅ (tag: develop) | ✅ |
| Push (main) | ✅ | ❌ | ✅ (tag: main, latest) | ✅ |
| Pull Request | ✅ | ❌ | ❌ | ❌ |
| Tag (v*.*.*) | ✅ | ✅ | ✅ (multi-tags) | ✅ |
| Manual | ✅ | ⚙️ (option) | ✅ | ✅ |

## 🏷️ Stratégie de tagging Docker

### Pour un tag `v1.2.3`

```
ghcr.io/tiogars/starter-api-spring-mysql:v1.2.3    ← Version exacte
ghcr.io/tiogars/starter-api-spring-mysql:1.2       ← Mineur (reçoit patches)
ghcr.io/tiogars/starter-api-spring-mysql:1         ← Majeur (reçoit mineurs)
ghcr.io/tiogars/starter-api-spring-mysql:latest    ← Dernière stable (main)
```

### Pour une branche

```
ghcr.io/tiogars/starter-api-spring-mysql:develop           ← Branche develop
ghcr.io/tiogars/starter-api-spring-mysql:main              ← Branche main
ghcr.io/tiogars/starter-api-spring-mysql:main-abc1234     ← SHA commit
```

## 🔐 Sécurité et permissions

### Tokens et authentification

```
┌─────────────────────────┐
│   GITHUB_TOKEN          │  Fourni automatiquement par GitHub Actions
│   (automatique)         │  Permissions : read/write packages, contents, issues
└─────────────────────────┘
          │
          ├─> GitHub Packages (Maven dependencies)
          ├─> GitHub Container Registry (Docker)
          ├─> GitHub Releases (Artifacts)
          └─> GitHub Security (SARIF reports)

┌─────────────────────────┐
│   Personal Access Token │  Configuration locale uniquement
│   (développeur)         │  Permissions : read:packages
└─────────────────────────┘
          │
          └─> Maven local (~/.m2/settings.xml)
```

### Permissions des jobs

| Job | contents | packages | security-events | id-token |
|-----|----------|----------|-----------------|----------|
| Build & Test | read | read | - | - |
| Create Release | write | read | - | - |
| Docker Push | read | write | - | write |
| Security Scan | read | read | write | - |

## 📦 Gestion des dépendances

```
┌───────────────────────────────────────────────┐
│         Dépendances du projet                 │
├───────────────────────────────────────────────┤
│                                               │
│  Maven Central                                │
│  ├─ Spring Boot 3.5.7                        │
│  ├─ MySQL Connector                          │
│  ├─ SpringDoc OpenAPI                        │
│  └─ [autres dépendances publiques]           │
│                                               │
│  GitHub Packages (tiogars)                    │
│  ├─ architecture-create-service (1.0.2)      │
│  └─ architecture-select-service (1.0.0)      │
│                                               │
└───────────────────────────────────────────────┘
         │                        │
         ▼                        ▼
┌─────────────────┐    ┌──────────────────┐
│  Maven Central  │    │ GitHub Packages  │
│  (public)       │    │  (authentified)  │
└─────────────────┘    └──────────────────┘
```

### Workflow de mise à jour

Le workflow `dependency-check.yml` s'exécute automatiquement chaque lundi :

```
Monday 9:00 UTC
    │
    ├─> Check Maven Central updates
    ├─> Check GitHub Packages updates
    ├─> Check Plugin updates
    │
    ├─> Generate report
    │
    └─> Create/Update GitHub Issue
            │
            └─> Label: "dependencies", "maintenance"
```

## 🎯 Résumé des workflows

### ci-cd.yml (Principal)
- **Déclencheurs** : push, PR, tags, manual
- **Jobs** : 4 (build, release, docker, security)
- **Durée** : ~5-10 minutes
- **Artifacts** : JAR, Docker images, SBOM, reports

### dependency-check.yml (Maintenance)
- **Déclencheurs** : schedule (weekly), manual
- **Jobs** : 1 (check)
- **Durée** : ~2-3 minutes
- **Output** : GitHub Issue

## 🚀 Points d'entrée pour les utilisateurs

### Développeur (Local)

```powershell
.\cicd.ps1 setup      # Configuration initiale
.\cicd.ps1 build      # Build quotidien
.\cicd.ps1 test       # Tests
.\cicd.ps1 release    # Créer une release
```

### DevOps / Release Manager

```bash
# Via GitHub interface
Actions → CI/CD Pipeline → Run workflow

# Via Git
git tag -a v1.0.0 -m "Release 1.0.0"
git push origin v1.0.0
```

### Utilisateur final

```bash
# Docker
docker pull ghcr.io/tiogars/starter-api-spring-mysql:latest
docker run -d -p 8080:8080 ghcr.io/tiogars/starter-api-spring-mysql:latest

# JAR (depuis release)
wget https://github.com/tiogars/starter-api-spring-mysql/releases/download/v1.0.0/starter-1.0.0.jar
java -jar starter-1.0.0.jar
```

---

**Dernière mise à jour** : 2025-10-24  
**Version** : 1.0  
**Maintenu par** : Tiogars
