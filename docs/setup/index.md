# Setup Documentation

This section contains documentation for setting up and configuring the project.

## Available Documentation

### [GitHub Packages Authentication Setup](GITHUB_PACKAGES_SETUP.md)

Complete guide for authenticating with GitHub Packages to access Maven dependencies.

**Topics covered:**

- GitHub Actions workflow authentication
- Local development setup with Personal Access Tokens
- Troubleshooting authentication issues
- Security best practices

---

## Quick Start

For local development, the most common setup task is configuring Maven to access GitHub Packages:

1. Copy the settings template: `cp settings.xml.example ~/.m2/settings.xml`
2. Create a GitHub Personal Access Token with `read:packages` permission
3. Update `~/.m2/settings.xml` with your GitHub username and token
4. Run `mvn clean install` to verify

See [GitHub Packages Setup](GITHUB_PACKAGES_SETUP.md) for detailed instructions.
