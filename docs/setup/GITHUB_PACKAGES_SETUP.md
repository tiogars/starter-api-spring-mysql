# GitHub Packages Authentication Setup

## Overview

This project uses GitHub Packages to host and retrieve Maven dependencies. Specifically, it depends on:

- `fr.tiogars:architecture-create-service:1.0.3`
- `fr.tiogars:architecture-select-service:1.0.0`

These packages are hosted at `https://maven.pkg.github.com/tiogars/*`.

## GitHub Actions (CI/CD)

### Authentication in Workflows

All GitHub Actions workflows that run Maven commands requiring access to GitHub Packages must include the
`GITHUB_TOKEN` environment variable. This token is automatically provided by GitHub Actions.

**Correct workflow configuration:**

```yaml
- name: Setup Java
  uses: actions/setup-java@v5
  with:
    java-version: '21'
    distribution: 'temurin'
    cache: 'maven'
    server-id: github
    server-username: ${{ github.actor }}
    server-password: ${{ secrets.GITHUB_TOKEN }}

- name: Build with Maven
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
  run: mvn clean install
```

**Key points:**

1. The `actions/setup-java` action creates a Maven `settings.xml` file with server credentials
2. The `server-id` must match the repository ID in `pom.xml` (in this case: `github`)
3. The `GITHUB_TOKEN` environment variable must be set when running Maven commands
4. The `GITHUB_TOKEN` is automatically available as `${{ secrets.GITHUB_TOKEN }}` in GitHub Actions

### Required Permissions

Workflows must have the `packages: read` permission to access GitHub Packages:

```yaml
permissions:
  contents: read
  packages: read
```

## Local Development

### Setup for Local Machine

To build the project locally, you need to authenticate with GitHub Packages.

#### Step 1: Create a Personal Access Token (PAT)

1. Go to <https://github.com/settings/tokens>
2. Click "Generate new token (classic)"
3. Give it a descriptive name (e.g., "Maven GitHub Packages")
4. Select the `read:packages` permission
5. Generate and copy the token

#### Step 2: Configure Maven Settings

Create or update your Maven `settings.xml` file:

**Linux/Mac:** `~/.m2/settings.xml`  
**Windows:** `%USERPROFILE%\.m2\settings.xml`

Use the provided template:

**Linux/Mac:**

```bash
cp settings.xml.example ~/.m2/settings.xml
```

**Windows (PowerShell):**

```powershell
Copy-Item settings.xml.example $env:USERPROFILE\.m2\settings.xml
```

**Windows (Command Prompt):**

```cmd
copy settings.xml.example %USERPROFILE%\.m2\settings.xml
```

Then edit the `settings.xml` file in your `.m2` directory and replace the placeholder values with your credentials.

**Placeholders to replace:**

- `VOTRE_USERNAME_GITHUB` → your GitHub username
- `VOTRE_PERSONAL_ACCESS_TOKEN` → the PAT you created

> **Note:** The template file uses French placeholders ("VOTRE" means "YOUR" in French). Replace them with your actual values.

**Example:**

```xml
<servers>
  <server>
    <id>github</id>
    <username>your-github-username</username>
    <password>ghp_your_personal_access_token_here</password>
  </server>
</servers>
```

#### Step 3: Verify the Setup

Run a Maven build to verify the setup:

```bash
mvn clean install
```

If successful, Maven will download the dependencies from GitHub Packages.

## Troubleshooting

### Error: 401 Unauthorized

**Symptom:**

```text
Could not transfer artifact fr.tiogars:architecture-create-service:pom:1.0.3 from/to github
(https://maven.pkg.github.com/tiogars/*): status code: 401, reason phrase: Unauthorized (401)
```

**Causes and Solutions:**

1. **Missing GITHUB_TOKEN in workflows**
   - Solution: Ensure the `GITHUB_TOKEN` environment variable is set in the step that runs Maven

2. **Incorrect or expired Personal Access Token (local development)**
   - Solution: Generate a new PAT with `read:packages` permission and update `~/.m2/settings.xml`

3. **Missing or incorrect server configuration**
   - Solution: Verify that the `server-id` in the workflow matches the repository ID in `pom.xml`

4. **Missing permissions in workflow**
   - Solution: Add `packages: read` to the workflow permissions

### Verifying Workflow Configuration

Check that your workflow includes:

```yaml
permissions:
  packages: read  # Required to read from GitHub Packages

steps:
  - uses: actions/setup-java@v5
    with:
      server-id: github  # Must match repository ID in pom.xml
      server-username: ${{ github.actor }}
      server-password: ${{ secrets.GITHUB_TOKEN }}

  - name: Maven build
    env:
      GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}  # Required!
    run: mvn verify
```

## Security Best Practices

1. **Never commit** `settings.xml` with credentials to Git
2. **Never share** your Personal Access Token
3. **Use scoped tokens** - only grant `read:packages` permission
4. **Rotate tokens** regularly and when team members leave
5. **Use GitHub Actions secrets** for CI/CD, not hardcoded tokens

## Related Files

- [`pom.xml`](../../pom.xml) - Maven project configuration with GitHub Packages repository
- [`settings.xml.example`](../../settings.xml.example) - Template for local Maven settings
- [`.github/workflows/build.yml`](../../.github/workflows/build.yml) - Build workflow configuration
- [`.github/workflows/release.yml`](../../.github/workflows/release.yml) - Release workflow configuration
- [`.github/workflows/maven-site.yml`](../../.github/workflows/maven-site.yml) - Documentation workflow

## Additional Resources

- [GitHub Packages Documentation](https://docs.github.com/en/packages)
- [Authenticating to GitHub Packages](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)
- [Creating Personal Access Tokens](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
