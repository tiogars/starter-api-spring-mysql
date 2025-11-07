# 📋 Release Checklist

## Before creating a release

### 🔍 Pre-checks

- [ ] All tests pass locally (`./mvnw.cmd test`)
- [ ] The application compiles without error (`./mvnw.cmd clean compile`)
- [ ] Full build succeeds (`./mvnw.cmd clean verify`)
- [ ] No critical vulnerabilities in dependencies

### 📝 Documentation

- [ ] `README.md` is up to date
- [ ] New features are documented
- [ ] Code examples work
- [ ] Environment variables are documented
- [ ] `CHANGELOG.md` updated (if applicable)

### 🗄️ Database

- [ ] Schema migrations are tested
- [ ] SQL scripts validated
- [ ] Test data works
- [ ] Backward compatibility verified

### 🔧 Configuration

- [ ] `application.yml` is correct
- [ ] Spring profiles (dev/test/prod) configured
- [ ] Environment variables documented in `.env.example`
- [ ] Secrets are NOT hardcoded in code

### 🐳 Docker

- [ ] Dockerfile builds (`docker build -t test .`)
- [ ] Docker image starts (`docker run test`)
- [ ] `docker-compose.yml` works (`docker-compose up`)
- [ ] Docker healthcheck responds correctly

### 🔐 Security

- [ ] No secrets present in code
- [ ] Dependencies are up to date
- [ ] Sensitive ports are not exposed
- [ ] Sensitive env vars come from secrets

### 📦 Version

- [ ] Version in `pom.xml` updated
- [ ] Version follows Semantic Versioning (X.Y.Z)
- [ ] Git tag will be `vX.Y.Z`

## Create the release

### Option 1: PowerShell script (recommended)

```powershell
# Replace X.Y.Z with your version
./cicd.ps1 release X.Y.Z
```

The script will:

1. ✅ Validate the version format
2. 📝 Update `pom.xml`
3. 🧪 Run tests
4. 🏷️ Create the Git tag
5. 🚀 Offer to push to GitHub

### Option 2: Manual

```powershell
# 1) Update version in pom.xml
# <version>X.Y.Z</version>

# 2) Test
./mvnw.cmd clean verify

# 3) Commit
git add pom.xml
git commit -m "chore: bump version to X.Y.Z"

# 4) Create tag
git tag -a vX.Y.Z -m "Release version X.Y.Z"

# 5) Push
git push origin main
git push origin vX.Y.Z
```

## After creating the tag

### 🔄 Check the CI/CD workflow

1. [ ] Go to <https://github.com/tiogars/starter-api-spring-mysql/actions>
2. [ ] Ensure the "CI/CD Pipeline" is running
3. [ ] Wait for all jobs to be ✅ green

### 📦 Verify artifacts

1. [ ] GitHub Release created: <https://github.com/tiogars/starter-api-spring-mysql/releases>
2. [ ] JAR attached to the release
3. [ ] Source archives (zip and tar.gz) attached
4. [ ] Changelog auto-generated

### 🐳 Verify Docker image

```powershell
# Pull the image
docker pull ghcr.io/tiogars/starter-api-spring-mysql:vX.Y.Z

# Check tags
docker pull ghcr.io/tiogars/starter-api-spring-mysql:latest
docker pull ghcr.io/tiogars/starter-api-spring-mysql:X.Y
docker pull ghcr.io/tiogars/starter-api-spring-mysql:X

# Test the image
docker run -d -p 8080:8080 ghcr.io/tiogars/starter-api-spring-mysql:vX.Y.Z
curl http://localhost:8080/actuator/health
```

### 🔐 Verify security

1. [ ] Check the Security tab on GitHub
2. [ ] Ensure no new vulnerabilities are reported
3. [ ] Review the generated SBOM in artifacts

## Post-release

### 📢 Communication

- [ ] Announce the release on relevant channels
- [ ] Update external docs if needed
- [ ] Notify users of any breaking changes (if applicable)

### 🔄 Prepare the next version

```powershell
# Create a develop branch for the next sprint
git checkout -b develop
git push -u origin develop
```

### 📊 Monitoring

- [ ] Monitor production logs
- [ ] Check Actuator metrics
- [ ] Review GitHub issues for new bugs

## 🆘 If something goes wrong

### Workflow failed

1. Review logs in the Actions tab
2. Fix the issue
3. Delete the tag: `git tag -d vX.Y.Z && git push origin :refs/tags/vX.Y.Z`
4. Retry the release

### Docker image won’t start

1. Check logs: `docker logs <container-id>`
2. Verify environment variables
3. Test locally with `docker-compose up`
4. Create a hotfix if needed

### Rollback needed

```powershell
# Mark the release as pre-release on GitHub
# Or create a new patch release

# Current problematic version: vX.Y.Z
# Create a patch: vX.Y.(Z+1)
./cicd.ps1 release X.Y.(Z+1)
```

## 📝 Notes

- Keep this checklist updated
- Document issues to improve the process
- Automate where possible

---

**Last updated**: 2025-10-24  
**Maintainer**: Tiogars
