# Guide de démarrage rapide - CI/CD

## 🚀 Premiers pas

### 1. Configuration initiale (une seule fois)

#### A. Configurer Maven localement pour GitHub Packages

1. Créez un Personal Access Token (PAT) sur GitHub :
   - Allez sur https://github.com/settings/tokens
   - Cliquez sur "Generate new token (classic)"
   - Nom : `Maven GitHub Packages`
   - Permissions : cochez `read:packages` et `write:packages`
   - Générez et **copiez le token**

2. Configurez Maven :
   ```bash
   # Windows
   copy settings.xml.example %USERPROFILE%\.m2\settings.xml
   notepad %USERPROFILE%\.m2\settings.xml
   
   # Linux/Mac
   cp settings.xml.example ~/.m2/settings.xml
   nano ~/.m2/settings.xml
   ```

3. Remplacez dans le fichier :
   - `VOTRE_USERNAME_GITHUB` → votre username GitHub
   - `VOTRE_PERSONAL_ACCESS_TOKEN` → le token copié à l'étape 1

#### B. Vérifier la configuration

```bash
# Compiler le projet
./mvnw clean compile

# Exécuter les tests
./mvnw test

# Créer le package
./mvnw package
```

### 2. Workflow automatique

Le workflow CI/CD se déclenche automatiquement sur :

#### Push sur main ou develop
```bash
git add .
git commit -m "feat: nouvelle fonctionnalité"
git push origin develop
```

**Résultat :** 
- ✅ Build et tests
- 🐳 Image Docker créée avec tag `develop`

#### Pull Request
```bash
git checkout -b feature/ma-fonctionnalite
git add .
git commit -m "feat: ajout de la fonctionnalité"
git push origin feature/ma-fonctionnalite
# Créez une PR sur GitHub
```

**Résultat :**
- ✅ Build et tests
- ❌ Pas de release ni push Docker

#### Release avec bump de version automatique
```bash
# Bump patch (0.0.1 -> 0.0.2)
.\cicd.ps1 release patch

# Bump minor (0.0.1 -> 0.1.0)
.\cicd.ps1 release minor

# Bump major (0.0.1 -> 1.0.0)
.\cicd.ps1 release major
```

**Résultat :**
- ✅ Version bump dans pom.xml
- ✅ Commit automatique
- ✅ Tag créé et poussé
- ✅ Build et tests
- 📦 Release GitHub créée avec artifacts
- 🐳 Images Docker avec tags multiples :
  - `ghcr.io/tiogars/starter-api-spring-mysql:v1.0.0`
  - `ghcr.io/tiogars/starter-api-spring-mysql:1.0`
  - `ghcr.io/tiogars/starter-api-spring-mysql:1`
  - `ghcr.io/tiogars/starter-api-spring-mysql:latest` (si sur main)
- 🔒 Scan de sécurité

### 3. Déclencher manuellement

1. Allez sur GitHub : `Actions` → `CI/CD Pipeline`
2. Cliquez sur `Run workflow`
3. Sélectionnez la branche
4. Cochez `Create a GitHub release` si vous voulez créer une release
5. Cliquez sur `Run workflow`

## 📦 Utiliser les artifacts

### Télécharger le JAR depuis une release

```bash
# Via l'interface GitHub
# Allez sur https://github.com/tiogars/starter-api-spring-mysql/releases

# Ou avec curl
curl -L -H "Authorization: token YOUR_PAT" \
  https://github.com/tiogars/starter-api-spring-mysql/releases/download/v1.0.0/starter-1.0.0.jar \
  -o starter.jar

# Lancer l'application
java -jar starter.jar
```

### Utiliser l'image Docker

```bash
# Connexion à GitHub Container Registry (première fois)
echo YOUR_PAT | docker login ghcr.io -u YOUR_USERNAME --password-stdin

# Pull de l'image
docker pull ghcr.io/tiogars/starter-api-spring-mysql:latest

# Lancer avec docker-compose (recommandé)
docker-compose up -d

# Ou lancer manuellement
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/starterdb \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  --name starter-api \
  ghcr.io/tiogars/starter-api-spring-mysql:latest

# Vérifier les logs
docker logs -f starter-api

# Accéder à l'API
curl http://localhost:8080/actuator/health
```

## 🔍 Vérifier le statut du workflow

### Via l'interface GitHub
1. Allez sur l'onglet `Actions`
2. Cliquez sur un workflow pour voir les détails
3. Consultez les logs de chaque job

### Via l'API GitHub
```bash
# Lister les workflows
curl -H "Authorization: token YOUR_PAT" \
  https://api.github.com/repos/tiogars/starter-api-spring-mysql/actions/runs

# Détails d'un run spécifique
curl -H "Authorization: token YOUR_PAT" \
  https://api.github.com/repos/tiogars/starter-api-spring-mysql/actions/runs/RUN_ID
```

## 🐛 Dépannage

### Erreur : "Could not find artifact architecture-create-service"

**Cause :** Les dépendances GitHub ne sont pas accessibles

**Solution :**
1. Vérifiez que le PAT a les permissions `read:packages`
2. Vérifiez que le fichier `~/.m2/settings.xml` est correctement configuré
3. Vérifiez que les packages existent sur https://github.com/tiogars?tab=packages

### Erreur : "docker: permission denied"

**Cause :** Votre utilisateur n'a pas les droits Docker

**Solution :**
```bash
# Linux
sudo usermod -aG docker $USER
newgrp docker

# Windows
# Exécutez Docker Desktop en tant qu'administrateur
```

### Build échoue sur GitHub Actions

**Solution :**
1. Vérifiez les logs dans l'onglet Actions
2. Assurez-vous que les dépendances sont accessibles publiquement ou avec le GITHUB_TOKEN
3. Vérifiez que les tests passent en local : `./mvnw test`

### Image Docker ne démarre pas

**Solution :**
```bash
# Vérifier les logs
docker logs starter-api

# Vérifier la configuration de la base de données
docker exec -it starter-api env | grep SPRING

# Tester la connexion à MySQL
docker exec -it starter-api nc -zv mysql 3306
```

## 📊 Monitoring

### Health check
```bash
curl http://localhost:8080/actuator/health
```

### Métriques
```bash
curl http://localhost:8080/actuator/metrics
```

### Info de l'application
```bash
curl http://localhost:8080/actuator/info
```

### OpenAPI/Swagger
Ouvrez dans votre navigateur : http://localhost:8080/swagger-ui.html

## 🔐 Sécurité

### Consulter les vulnérabilités

1. Allez sur GitHub → `Security` → `Code scanning`
2. Consultez les alertes Trivy

### Mettre à jour les dépendances

```bash
# Vérifier les mises à jour disponibles
./mvnw versions:display-dependency-updates

# Mettre à jour vers les dernières versions
./mvnw versions:use-latest-versions

# Commit et push pour relancer le scan
git add pom.xml
git commit -m "chore: update dependencies"
git push
```

## 📝 Conventions de commit

Utilisez [Conventional Commits](https://www.conventionalcommits.org/) :

- `feat:` nouvelle fonctionnalité
- `fix:` correction de bug
- `docs:` documentation
- `style:` formatage
- `refactor:` refactoring
- `test:` ajout de tests
- `chore:` tâches de maintenance

**Exemples :**
```bash
git commit -m "feat: add user authentication"
git commit -m "fix: resolve database connection issue"
git commit -m "docs: update API documentation"
```

## 🎯 Checklist avant release

- [ ] Tous les tests passent localement
- [ ] La version dans `pom.xml` est mise à jour
- [ ] Le CHANGELOG est à jour (optionnel, généré auto)
- [ ] Les migrations de base de données sont testées
- [ ] La documentation est à jour
- [ ] Les variables d'environnement sont documentées

```bash
# Vérification complète
./mvnw clean verify
docker-compose up --build
# Tester manuellement l'application
docker-compose down

# Si tout est OK, créer la release
git tag -a v1.0.0 -m "Release 1.0.0"
git push origin v1.0.0
```

## 📞 Support

- **Issues GitHub :** https://github.com/tiogars/starter-api-spring-mysql/issues
- **Documentation Actions :** https://docs.github.com/actions
- **Documentation Spring Boot :** https://docs.spring.io/spring-boot/docs/current/reference/html/
