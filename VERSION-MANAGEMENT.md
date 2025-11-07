# 🔢 Version Management with Maven

## Overview

This project uses the **Maven Versions Plugin** and the **Build Helper Maven Plugin** to automate versioning following **Semantic Versioning**.

## 🎯 Semantic Versioning (SemVer)

Format: `MAJOR.MINOR.PATCH`

- MAJOR: Incompatible API changes
- MINOR: Backward-compatible feature additions
- PATCH: Backward-compatible bug fixes

### Examples

```text
1.0.0 -> 1.0.1  (patch): Bug fix
1.0.1 -> 1.1.0  (minor): New feature
1.1.0 -> 2.0.0  (major): Breaking change
```

## 🚀 Automatic version bump

### Recommended method: via GitHub Actions

```powershell
# Patch bump (0.0.1 -> 0.0.2)
./cicd.ps1 release patch

# Minor bump (0.0.1 -> 0.1.0)
./cicd.ps1 release minor

# Major bump (0.0.1 -> 1.0.0)
./cicd.ps1 release major
```

The workflow automatically:

1. ✅ Removes `-SNAPSHOT` from the version
2. ✅ Applies the bump (patch/minor/major)
3. ✅ Updates `pom.xml`
4. ✅ Commits changes with `[skip ci]`
5. ✅ Creates and pushes the tag
6. ✅ Triggers build, test, release, and Docker

## 📋 Useful Maven commands

```powershell
# Show current version
./mvnw.cmd help:evaluate -Dexpression=project.version -q -DforceStdout

# Set a specific version
./mvnw.cmd versions:set -DnewVersion=1.2.3

# Show outdated dependencies
./mvnw.cmd versions:display-dependency-updates
```

## 📚 Full documentation

See [CHANGELOG-VERSION-MANAGEMENT.md](CHANGELOG-VERSION-MANAGEMENT.md) for the change summary and [QUICKSTART.md](QUICKSTART.md) for usage.
