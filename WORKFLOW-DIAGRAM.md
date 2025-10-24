# 🔄 Flux de gestion de version automatique

## Workflow visuel complet

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                        DÉVELOPPEUR                                          │
│                                                                             │
│  Commande :  .\cicd.ps1 release patch                                      │
│                          ↓                                                  │
└──────────────────────────┼──────────────────────────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                   GITHUB CLI (gh)                                           │
│                                                                             │
│  gh workflow run ci-cd.yml                                                  │
│    --field version_bump=patch                                               │
│    --field create_release=true                                              │
│                          ↓                                                  │
└──────────────────────────┼──────────────────────────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                   GITHUB ACTIONS                                            │
│                   Job: version-bump                                         │
│                                                                             │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ 1. Checkout code                                                  │     │
│  │    git clone + fetch                                              │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ 2. Setup Java 21 + Maven                                          │     │
│  │    Cache des dépendances Maven                                    │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ 3. Configure Maven settings.xml                                   │     │
│  │    Authentification GitHub Packages                               │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ 4. Get current version                                            │     │
│  │    mvnw help:evaluate -Dexpression=project.version                │     │
│  │    Résultat: 0.0.1-SNAPSHOT                                       │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ 5. Remove SNAPSHOT                                                │     │
│  │    mvnw versions:set -DremoveSnapshot                             │     │
│  │    0.0.1-SNAPSHOT → 0.0.1                                         │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ 6. Bump version (patch)                                           │     │
│  │    mvnw build-helper:parse-version versions:set                   │     │
│  │    -DnewVersion=${parsedVersion.majorVersion}.${...}.${next...}   │     │
│  │    0.0.1 → 0.0.2                                                  │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ 7. Commit changes                                                 │     │
│  │    git add pom.xml                                                │     │
│  │    git commit -m "chore: bump version to 0.0.2 [skip ci]"        │     │
│  │    git push origin main                                           │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ 8. Create and push tag                                            │     │
│  │    git tag -a v0.0.2 -m "Release version 0.0.2"                  │     │
│  │    git push origin v0.0.2                                         │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
└──────────────────────────┼──────────────────────────────────────────────────┘
                           │
                           ↓ (Tag v0.0.2 déclenche le workflow)
┌─────────────────────────────────────────────────────────────────────────────┐
│                   GITHUB ACTIONS                                            │
│                   Jobs: build-and-test, create-release, docker, security    │
│                                                                             │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ Job 1: Build and Test                                             │     │
│  │ ├─ Compile with Maven                                             │     │
│  │ ├─ Run tests                                                      │     │
│  │ ├─ Package JAR                                                    │     │
│  │ └─ Upload artifacts                                               │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ Job 2: Create Release                                             │     │
│  │ ├─ Download artifacts                                             │     │
│  │ ├─ Create source archives                                         │     │
│  │ ├─ Generate changelog                                             │     │
│  │ └─ Create GitHub Release                                          │     │
│  │    - JAR file                                                     │     │
│  │    - Sources (zip, tar.gz)                                        │     │
│  │    - Changelog                                                    │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ Job 3: Docker Build & Push                                        │     │
│  │ ├─ Build multi-platform (amd64, arm64)                            │     │
│  │ ├─ Tag images:                                                    │     │
│  │ │  - ghcr.io/.../starter-api:v0.0.2                              │     │
│  │ │  - ghcr.io/.../starter-api:0.0                                 │     │
│  │ │  - ghcr.io/.../starter-api:0                                   │     │
│  │ │  - ghcr.io/.../starter-api:latest                              │     │
│  │ ├─ Push to GitHub Container Registry                              │     │
│  │ └─ Generate SBOM                                                  │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
│  ┌───────────────────────────────────────────────────────────────────┐     │
│  │ Job 4: Security Scan                                              │     │
│  │ ├─ Pull Docker image                                              │     │
│  │ ├─ Run Trivy scanner                                              │     │
│  │ └─ Upload SARIF to GitHub Security                                │     │
│  └───────────────────────────────────────────────────────────────────┘     │
│                          ↓                                                  │
└──────────────────────────┼──────────────────────────────────────────────────┘
                           │
                           ↓
┌─────────────────────────────────────────────────────────────────────────────┐
│                        RÉSULTATS                                            │
│                                                                             │
│  ✅ Version bumped: 0.0.1-SNAPSHOT → 0.0.2                                 │
│  ✅ Commit pushed to main                                                   │
│  ✅ Tag v0.0.2 created                                                      │
│  ✅ GitHub Release published                                                │
│  ✅ Docker images available                                                 │
│  ✅ Security scan completed                                                 │
│                                                                             │
│  📦 GitHub Release:                                                         │
│     https://github.com/tiogars/starter-api-spring-mysql/releases/v0.0.2    │
│                                                                             │
│  🐳 Docker Images:                                                          │
│     ghcr.io/tiogars/starter-api-spring-mysql:v0.0.2                        │
│     ghcr.io/tiogars/starter-api-spring-mysql:0.0                           │
│     ghcr.io/tiogars/starter-api-spring-mysql:0                             │
│     ghcr.io/tiogars/starter-api-spring-mysql:latest                        │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

## Comparaison des types de bump

```
Version actuelle: 1.2.3

┌──────────┬─────────────┬────────────────────────────────┐
│   Type   │  Nouvelle   │         Quand utiliser         │
│          │   Version   │                                │
├──────────┼─────────────┼────────────────────────────────┤
│  patch   │    1.2.4    │ • Correction de bugs           │
│          │             │ • Améliorations mineures       │
│          │             │ • Patches de sécurité          │
├──────────┼─────────────┼────────────────────────────────┤
│  minor   │    1.3.0    │ • Nouvelle fonctionnalité      │
│          │             │ • Ajout rétrocompatible        │
│          │             │ • Amélioration majeure         │
├──────────┼─────────────┼────────────────────────────────┤
│  major   │    2.0.0    │ • Breaking changes             │
│          │             │ • Refonte complète             │
│          │             │ • Changement d'architecture    │
└──────────┴─────────────┴────────────────────────────────┘
```

## Timeline d'exécution

```
T+0s    Développeur exécute: .\cicd.ps1 release patch
T+2s    GitHub CLI déclenche le workflow
T+5s    Job version-bump démarre
T+30s   Setup environnement (Java, Maven, cache)
T+45s   Bump de version effectué
T+50s   Commit et push vers main
T+55s   Tag créé et poussé
T+60s   Job build-and-test démarre (déclenché par le tag)
T+2m    Compilation et tests complétés
T+3m    Job create-release démarre
T+4m    Release GitHub créée avec artifacts
T+5m    Job docker-build-push démarre
T+10m   Images Docker buildées et poussées (multi-arch)
T+12m   Job security-scan démarre
T+15m   Scan de sécurité terminé
T+15m   ✅ Pipeline complet terminé !
```

## Notifications

```
┌─────────────────────────────────────────────┐
│           Vous recevez                      │
├─────────────────────────────────────────────┤
│ 📧 Email de GitHub                          │
│    "Workflow run completed"                 │
│                                             │
│ 🔔 Notification GitHub                      │
│    "Release v0.0.2 published"               │
│                                             │
│ 📊 GitHub Step Summary                      │
│    Résumé détaillé dans Actions             │
└─────────────────────────────────────────────┘
```

---

**Temps total** : ~15 minutes de la commande à la release complète
**Interaction requise** : Une seule commande !
