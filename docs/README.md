# Documentation Index

Welcome to the starter-api-spring-mysql documentation. This directory contains all project documentation organized by theme.

## 📌 Quick Start & Reference

**New to the documentation?** Start here:

- **⭐ [Spring Cloud Config Quick Reference](setup/SPRING_CLOUD_CONFIG_QUICK_REFERENCE.md)** - Quick overview and decision guide
- **📄 [Adaptation Complete](ADAPTATION_COMPLETE.md)** - Executive summary of all documentation updates
- **📋 [Documentation Adaptation Summary](DOCUMENTATION_ADAPTATION_SUMMARY.md)** - Detailed breakdown of changes
- **✅ [Files Changed](FILES_CHANGED.md)** - Complete checklist and statistics

---

## Documentation Sections

### 📋 [Testing Documentation](testing/index.md)

Comprehensive guides on testing practices, coverage requirements, and quality standards.

**Key Topics:**

- Test coverage guide and best practices
- Test organization and structure
- Coverage reports and metrics
- Branch coverage strategies

**Quick Access:**

- [Test Coverage Guide](testing/TEST_COVERAGE_GUIDE.md)
- [Testing Summary](testing/TESTING_SUMMARY.md)
- [Branch Coverage Summary](testing/BRANCH_COVERAGE_SUMMARY.md)
- [Maven Site Reports](testing/MAVEN_SITE_REPORTS.md)

---

### 🔌 [API Documentation](api/index.md)

Documentation for all REST API endpoints and usage examples.

**Available APIs:**

- Search API - Query and filter samples
- Export API - Export data in various formats

**Quick Access:**

- [Search API](api/SEARCH_API.md)
- [Export API](api/EXPORT_API.md)

---

### 🔧 [Implementation Documentation](implementation/index.md)

Technical implementation details, architecture decisions, and project completion reports.

**Key Topics:**

- Implementation details and architecture
- Completion reports and milestones
- Technical patterns and decisions

**Quick Access:**

- [Implementation Details](implementation/IMPLEMENTATION_DETAILS.md)
- [Completion Report](implementation/COMPLETION_REPORT.md)

---

### ✨ [Features Documentation](features/index.md)

User-facing feature documentation and guides.

---

### ⚙️ [Setup Documentation](setup/index.md)

Project setup and configuration guides, including Docker deployment and centralized configuration management.

**Key Topics:**

- GitHub Packages authentication for Maven dependencies
- Local development environment setup
- Docker Compose deployment with MySQL
- Spring Cloud Config Server for centralized configuration
- Database connection resilience and troubleshooting

**Quick Access:**

- [Spring Cloud Config Server Integration](setup/SPRING_CLOUD_CONFIG.md)
- [Docker Deployment Guide](setup/DOCKER_DEPLOYMENT.md)
- [GitHub Packages Setup](setup/GITHUB_PACKAGES_SETUP.md)
- [Database Connection Resilience](setup/CONNECTION_RESILIENCE.md)

---

## Project Information

- **Framework**: Spring Boot 4.0.1 with Java 21
- **Database**: MySQL
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, JaCoCo
- **Code Quality**: SonarQube integration

## Contributing

When adding new documentation:

1. Place files in the appropriate thematic folder
2. Update the relevant `index.md` file
3. Update this main README if adding a new section
4. Follow naming conventions (see [Copilot Instructions](../.github/copilot-instructions.md))

## Quick Links

- [Project README](../README.md)
- [Copilot Instructions](../.github/copilot-instructions.md)
- [GitHub Repository](https://github.com/tiogars/starter-api-spring-mysql)
