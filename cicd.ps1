# Script PowerShell pour gérer le workflow CI/CD
# Usage: .\cicd.ps1 [command]

param(
    [Parameter(Position=0)]
    [ValidateSet('setup', 'build', 'test', 'package', 'release', 'docker-build', 'docker-run', 'status', 'help')]
    [string]$Command = 'help',
    
    [Parameter(Position=1)]
    [string]$Version = ""
)

$ErrorActionPreference = "Stop"

function Show-Help {
    Write-Host "🚀 Script de gestion CI/CD pour Starter API Spring Boot" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Usage: .\cicd.ps1 [command] [options]" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Commandes disponibles:" -ForegroundColor Green
    Write-Host "  setup         - Configure l'environnement local (Maven settings)"
    Write-Host "  build         - Compile l'application"
    Write-Host "  test          - Execute les tests"
    Write-Host "  package       - Crée le package JAR"
    Write-Host "  release       - Crée une nouvelle release (tag + push)"
    Write-Host "  docker-build  - Build l'image Docker localement"
    Write-Host "  docker-run    - Lance l'application avec Docker Compose"
    Write-Host "  status        - Affiche le statut du dernier workflow GitHub"
    Write-Host "  help          - Affiche cette aide"
    Write-Host ""
    Write-Host "Exemples:" -ForegroundColor Yellow
    Write-Host "  .\cicd.ps1 setup"
    Write-Host "  .\cicd.ps1 build"
    Write-Host "  .\cicd.ps1 release 1.0.0"
    Write-Host "  .\cicd.ps1 docker-run"
}

function Setup-Environment {
    Write-Host "🔧 Configuration de l'environnement..." -ForegroundColor Cyan
    
    $m2Dir = "$env:USERPROFILE\.m2"
    $settingsFile = "$m2Dir\settings.xml"
    
    if (-not (Test-Path $m2Dir)) {
        New-Item -ItemType Directory -Path $m2Dir -Force | Out-Null
        Write-Host "✅ Dossier .m2 créé" -ForegroundColor Green
    }
    
    if (Test-Path $settingsFile) {
        $response = Read-Host "Le fichier settings.xml existe déjà. Voulez-vous le remplacer? (o/N)"
        if ($response -ne "o") {
            Write-Host "⏭️  Configuration annulée" -ForegroundColor Yellow
            return
        }
    }
    
    Write-Host ""
    Write-Host "Pour configurer Maven avec GitHub Packages, vous avez besoin de:" -ForegroundColor Yellow
    Write-Host "1. Votre nom d'utilisateur GitHub"
    Write-Host "2. Un Personal Access Token avec la permission 'read:packages'"
    Write-Host ""
    Write-Host "Créer un token sur: https://github.com/settings/tokens" -ForegroundColor Cyan
    Write-Host ""
    
    $username = Read-Host "Entrez votre nom d'utilisateur GitHub"
    $token = Read-Host "Entrez votre Personal Access Token" -AsSecureString
    $tokenPlain = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto(
        [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($token)
    )
    
    $settingsContent = @"
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>$username</username>
      <password>$tokenPlain</password>
    </server>
  </servers>
  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>github</id>
          <name>GitHub Packages</name>
          <url>https://maven.pkg.github.com/tiogars/*</url>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>github</activeProfile>
  </activeProfiles>
</settings>
"@
    
    $settingsContent | Out-File -FilePath $settingsFile -Encoding UTF8
    Write-Host "✅ Fichier settings.xml créé avec succès!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Vous pouvez maintenant exécuter: .\cicd.ps1 build" -ForegroundColor Cyan
}

function Build-Application {
    Write-Host "🔨 Compilation de l'application..." -ForegroundColor Cyan
    & .\mvnw.cmd clean compile -B
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Compilation réussie!" -ForegroundColor Green
    } else {
        Write-Host "❌ Échec de la compilation" -ForegroundColor Red
        exit 1
    }
}

function Test-Application {
    Write-Host "🧪 Exécution des tests..." -ForegroundColor Cyan
    & .\mvnw.cmd test -B
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Tests réussis!" -ForegroundColor Green
    } else {
        Write-Host "❌ Échec des tests" -ForegroundColor Red
        exit 1
    }
}

function Package-Application {
    Write-Host "📦 Création du package..." -ForegroundColor Cyan
    & .\mvnw.cmd package -DskipTests -B
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Package créé avec succès!" -ForegroundColor Green
        $jarFile = Get-ChildItem -Path "target" -Filter "*.jar" -Exclude "*.original" | Select-Object -First 1
        if ($jarFile) {
            Write-Host "📁 Fichier JAR: $($jarFile.FullName)" -ForegroundColor Cyan
        }
    } else {
        Write-Host "❌ Échec de la création du package" -ForegroundColor Red
        exit 1
    }
}

function Create-Release {
    if (-not $Version) {
        Write-Host "❌ Type de bump requis. Usage: .\cicd.ps1 release <major|minor|patch>" -ForegroundColor Red
        Write-Host "Exemples:" -ForegroundColor Yellow
        Write-Host "  .\cicd.ps1 release patch   # 1.0.0 -> 1.0.1" -ForegroundColor Cyan
        Write-Host "  .\cicd.ps1 release minor   # 1.0.0 -> 1.1.0" -ForegroundColor Cyan
        Write-Host "  .\cicd.ps1 release major   # 1.0.0 -> 2.0.0" -ForegroundColor Cyan
        exit 1
    }
    
    # Validation du type de bump
    if ($Version -notmatch '^(major|minor|patch)$') {
        Write-Host "❌ Type de bump invalide. Utilisez: major, minor ou patch" -ForegroundColor Red
        exit 1
    }
    
    Write-Host "🏷️  Déclenchement du workflow de release avec bump $Version..." -ForegroundColor Cyan
    
    # Vérifier qu'on est sur la branche main
    $currentBranch = git branch --show-current
    if ($currentBranch -ne "main") {
        Write-Host "⚠️  Vous n'êtes pas sur la branche main (actuellement sur: $currentBranch)" -ForegroundColor Yellow
        $response = Read-Host "Voulez-vous continuer quand même? (o/N)"
        if ($response -ne "o") {
            Write-Host "Veuillez basculer sur main avec: git checkout main" -ForegroundColor Yellow
            exit 1
        }
    }
    
    # Vérifier qu'il n'y a pas de modifications non commitées
    $status = git status --porcelain
    if ($status) {
        Write-Host "⚠️  Modifications non commitées détectées:" -ForegroundColor Yellow
        Write-Host $status
        $response = Read-Host "Voulez-vous continuer quand même? (o/N)"
        if ($response -ne "o") {
            exit 1
        }
    }
    
    # Vérifier si gh CLI est installé
    $ghExists = Get-Command gh -ErrorAction SilentlyContinue
    if (-not $ghExists) {
        Write-Host "⚠️  GitHub CLI (gh) n'est pas installé" -ForegroundColor Yellow
        Write-Host "Installez-le depuis: https://cli.github.com/" -ForegroundColor Cyan
        Write-Host "" 
        Write-Host "Alternative : Déclenchez manuellement le workflow sur GitHub :" -ForegroundColor Yellow
        Write-Host "https://github.com/tiogars/starter-api-spring-mysql/actions/workflows/ci-cd.yml" -ForegroundColor Cyan
        exit 1
    }
    
    # Obtenir la version actuelle
    Write-Host "📋 Récupération de la version actuelle..." -ForegroundColor Cyan
    $currentVersion = & .\mvnw.cmd help:evaluate -Dexpression=project.version -q -DforceStdout
    Write-Host "Version actuelle: $currentVersion" -ForegroundColor Yellow
    
    Write-Host ""
    Write-Host "📝 Résumé de la release:" -ForegroundColor Cyan
    Write-Host "  Type de bump: $Version" -ForegroundColor White
    Write-Host "  Version actuelle: $currentVersion" -ForegroundColor White
    Write-Host "  Branche: $currentBranch" -ForegroundColor White
    Write-Host ""
    
    $response = Read-Host "Confirmer le déclenchement du workflow? (o/N)"
    if ($response -ne "o") {
        Write-Host "❌ Annulé" -ForegroundColor Red
        exit 0
    }
    
    # Déclencher le workflow GitHub Actions
    Write-Host "🚀 Déclenchement du workflow..." -ForegroundColor Cyan
    gh workflow run ci-cd.yml --field version_bump=$Version --field create_release=true
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Workflow déclenché avec succès!" -ForegroundColor Green
        Write-Host ""
        Write-Host "🔗 Suivez l'exécution sur:" -ForegroundColor Cyan
        Write-Host "   https://github.com/tiogars/starter-api-spring-mysql/actions" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "📋 Le workflow va:" -ForegroundColor Cyan
        Write-Host "   1. Faire le bump de version ($Version)" -ForegroundColor White
        Write-Host "   2. Commiter la nouvelle version dans pom.xml" -ForegroundColor White
        Write-Host "   3. Créer et pousser le tag" -ForegroundColor White
        Write-Host "   4. Lancer le build, tests et création de la release" -ForegroundColor White
    } else {
        Write-Host "❌ Erreur lors du déclenchement du workflow" -ForegroundColor Red
        exit 1
    }
}

function Build-Docker {
    Write-Host "🐳 Build de l'image Docker..." -ForegroundColor Cyan
    docker build -t starter-api:latest .
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Image Docker créée avec succès!" -ForegroundColor Green
        Write-Host "Pour lancer: docker run -p 8080:8080 starter-api:latest" -ForegroundColor Cyan
    } else {
        Write-Host "❌ Échec du build Docker" -ForegroundColor Red
        exit 1
    }
}

function Run-Docker {
    Write-Host "🚀 Lancement de l'application avec Docker Compose..." -ForegroundColor Cyan
    if (-not (Test-Path "docker-compose.yml")) {
        Write-Host "❌ Fichier docker-compose.yml introuvable" -ForegroundColor Red
        exit 1
    }
    
    docker-compose up -d
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Application démarrée!" -ForegroundColor Green
        Write-Host ""
        Write-Host "📍 URLs utiles:" -ForegroundColor Cyan
        Write-Host "  - Health: http://localhost:8080/actuator/health" -ForegroundColor Yellow
        Write-Host "  - Swagger: http://localhost:8080/swagger-ui.html" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "📋 Commandes utiles:" -ForegroundColor Cyan
        Write-Host "  - Logs: docker-compose logs -f" -ForegroundColor Yellow
        Write-Host "  - Stop: docker-compose down" -ForegroundColor Yellow
    } else {
        Write-Host "❌ Échec du démarrage" -ForegroundColor Red
        exit 1
    }
}

function Get-WorkflowStatus {
    Write-Host "📊 Récupération du statut du workflow..." -ForegroundColor Cyan
    
    # Vérifier si gh CLI est installé
    $ghExists = Get-Command gh -ErrorAction SilentlyContinue
    if (-not $ghExists) {
        Write-Host "⚠️  GitHub CLI (gh) n'est pas installé" -ForegroundColor Yellow
        Write-Host "Installez-le depuis: https://cli.github.com/" -ForegroundColor Cyan
        Write-Host "Ou consultez: https://github.com/tiogars/starter-api-spring-mysql/actions" -ForegroundColor Cyan
        return
    }
    
    gh run list --limit 5
}

# Exécution de la commande
switch ($Command) {
    'setup' { Setup-Environment }
    'build' { Build-Application }
    'test' { Test-Application }
    'package' { Package-Application }
    'release' { Create-Release }
    'docker-build' { Build-Docker }
    'docker-run' { Run-Docker }
    'status' { Get-WorkflowStatus }
    'help' { Show-Help }
    default { Show-Help }
}
