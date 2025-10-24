# 🔢 Gestion de la version avec Maven

## Vue d'ensemble

Le projet utilise le **Maven Versions Plugin** et le **Build Helper Maven Plugin** pour gérer automatiquement les versions selon la méthode **Semantic Versioning**.

## 🎯 Semantic Versioning (SemVer)

Format : `MAJOR.MINOR.PATCH`

- **MAJOR** : Changements incompatibles avec les versions précédentes
- **MINOR** : Ajout de fonctionnalités rétrocompatibles
- **PATCH** : Corrections de bugs rétrocompatibles

### Exemples

```
1.0.0 -> 1.0.1  (patch)  : Correction de bugs
1.0.1 -> 1.1.0  (minor)  : Nouvelle fonctionnalité
1.1.0 -> 2.0.0  (major)  : Changement majeur (breaking changes)
```

## 🚀 Bump de version automatique

### Méthode recommandée : Via GitHub Actions

```powershell
# Bump patch (0.0.1 -> 0.0.2)
.\cicd.ps1 release patch

# Bump minor (0.0.1 -> 0.1.0)
.\cicd.ps1 release minor

# Bump major (0.0.1 -> 1.0.0)
.\cicd.ps1 release major
```

Le workflow fait automatiquement :
1. ✅ Retire le `-SNAPSHOT` de la version
2. ✅ Applique le bump (patch/minor/major)
3. ✅ Met à jour `pom.xml`
4. ✅ Commit les changements `[skip ci]`
5. ✅ Crée et pousse le tag
6. ✅ Déclenche build, test, release et Docker

## 📋 Commandes Maven utiles

```bash
# Afficher la version actuelle
.\mvnw.cmd help:evaluate -Dexpression=project.version -q -DforceStdout

# Définir une version spécifique
.\mvnw.cmd versions:set -DnewVersion=1.2.3

# Vérifier les dépendances obsolètes
.\mvnw.cmd versions:display-dependency-updates
```

## 📚 Documentation complète

Consultez [VERSION-MANAGEMENT.md](VERSION-MANAGEMENT.md) pour plus de détails.
