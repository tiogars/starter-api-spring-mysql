# 📋 Checklist de Release

## Avant de créer une release

### 🔍 Vérifications préliminaires

- [ ] Tous les tests passent localement (`.\mvnw.cmd test`)
- [ ] L'application compile sans erreur (`.\mvnw.cmd clean compile`)
- [ ] Le build complet fonctionne (`.\mvnw.cmd clean verify`)
- [ ] Aucune dépendance n'a de vulnérabilités critiques connues

### 📝 Documentation

- [ ] Le README.md est à jour
- [ ] Les nouveautés sont documentées
- [ ] Les exemples de code fonctionnent
- [ ] Les variables d'environnement sont documentées
- [ ] Le fichier CHANGELOG.md est mis à jour (si applicable)

### 🗄️ Base de données

- [ ] Les migrations de schéma sont testées
- [ ] Les scripts SQL sont validés
- [ ] Les données de test fonctionnent
- [ ] La compatibilité avec les versions précédentes est vérifiée

### 🔧 Configuration

- [ ] Le fichier application.yml est correct
- [ ] Les profils Spring (dev/test/prod) sont configurés
- [ ] Les variables d'environnement sont documentées dans .env.example
- [ ] Les secrets ne sont PAS hardcodés dans le code

### 🐳 Docker

- [ ] Le Dockerfile build correctement (`docker build -t test .`)
- [ ] L'image Docker démarre (`docker run test`)
- [ ] Le fichier docker-compose.yml fonctionne (`docker-compose up`)
- [ ] Le healthcheck Docker répond correctement

### 🔐 Sécurité

- [ ] Aucun secret n'est présent dans le code
- [ ] Les dépendances sont à jour
- [ ] Les ports sensibles ne sont pas exposés
- [ ] Les variables d'environnement sensibles utilisent des secrets

### 📦 Version

- [ ] La version dans pom.xml est mise à jour
- [ ] Le format de version suit Semantic Versioning (X.Y.Z)
- [ ] Le tag Git sera au format vX.Y.Z

## Créer la release

### Option 1 : Avec le script PowerShell (Recommandé)

```powershell
# Remplacez X.Y.Z par votre version
.\cicd.ps1 release X.Y.Z
```

Le script va :
1. ✅ Valider le format de version
2. 📝 Mettre à jour pom.xml
3. 🧪 Exécuter les tests
4. 🏷️ Créer le tag Git
5. 🚀 Proposer de pousser vers GitHub

### Option 2 : Manuellement

```bash
# 1. Mettre à jour la version dans pom.xml
# <version>X.Y.Z</version>

# 2. Tester
.\mvnw.cmd clean verify

# 3. Commiter
git add pom.xml
git commit -m "chore: bump version to X.Y.Z"

# 4. Créer le tag
git tag -a vX.Y.Z -m "Release version X.Y.Z"

# 5. Pousser
git push origin main
git push origin vX.Y.Z
```

## Après la création du tag

### 🔄 Vérifier le workflow CI/CD

1. [ ] Allez sur https://github.com/tiogars/starter-api-spring-mysql/actions
2. [ ] Vérifiez que le workflow "CI/CD Pipeline" est en cours
3. [ ] Attendez que tous les jobs soient ✅ verts

### 📦 Vérifier les artifacts

1. [ ] Release GitHub créée : https://github.com/tiogars/starter-api-spring-mysql/releases
2. [ ] JAR attaché à la release
3. [ ] Archives sources (zip et tar.gz) attachées
4. [ ] Changelog généré automatiquement

### 🐳 Vérifier l'image Docker

```bash
# Pull de l'image
docker pull ghcr.io/tiogars/starter-api-spring-mysql:vX.Y.Z

# Vérifier les tags
docker pull ghcr.io/tiogars/starter-api-spring-mysql:latest
docker pull ghcr.io/tiogars/starter-api-spring-mysql:X.Y
docker pull ghcr.io/tiogars/starter-api-spring-mysql:X

# Tester l'image
docker run -d -p 8080:8080 ghcr.io/tiogars/starter-api-spring-mysql:vX.Y.Z
curl http://localhost:8080/actuator/health
```

### 🔐 Vérifier la sécurité

1. [ ] Consultez l'onglet Security sur GitHub
2. [ ] Vérifiez qu'il n'y a pas de nouvelles vulnérabilités détectées
3. [ ] Consultez le SBOM généré dans les artifacts

## Post-release

### 📢 Communication

- [ ] Annoncez la release sur les canaux appropriés
- [ ] Mettez à jour la documentation externe si nécessaire
- [ ] Notifiez les utilisateurs des breaking changes (si applicable)

### 🔄 Préparation de la prochaine version

```bash
# Créer une branche develop pour le prochain sprint
git checkout -b develop
git push -u origin develop
```

### 📊 Monitoring

- [ ] Surveillez les logs en production
- [ ] Vérifiez les métriques Actuator
- [ ] Consultez les issues GitHub pour les nouveaux bugs

## 🆘 En cas de problème

### Le workflow échoue

1. Consultez les logs dans l'onglet Actions
2. Corrigez le problème
3. Supprimez le tag : `git tag -d vX.Y.Z && git push origin :refs/tags/vX.Y.Z`
4. Recommencez la release après correction

### L'image Docker ne démarre pas

1. Vérifiez les logs : `docker logs <container-id>`
2. Vérifiez les variables d'environnement
3. Testez en local avec `docker-compose up`
4. Créez un hotfix si nécessaire

### Rollback nécessaire

```bash
# Marquer la release comme pre-release sur GitHub
# Ou créer une nouvelle release avec un patch

# Version actuelle problématique : vX.Y.Z
# Créer un patch : vX.Y.(Z+1)
.\cicd.ps1 release X.Y.(Z+1)
```

## 📝 Notes

- Conservez cette checklist à jour
- Documentez les problèmes rencontrés pour améliorer le processus
- Automatisez ce qui peut l'être

---

**Dernière mise à jour** : 2025-10-24
**Mainteneur** : Tiogars
