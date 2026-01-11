# GitHub Copilot Instructions for starter-api-spring-mysql

## Code Quality & Testing Standards

### Test Coverage Requirements

This project enforces **80% minimum code coverage** for:
- Line coverage
- Branch coverage  
- Instruction coverage

Coverage is validated by JaCoCo during the `mvn verify` phase and reported to SonarQube.

### When Reviewing Code

**Always ensure the following for code reviews:**

1. **Check Test Coverage**: Every new feature, bug fix, or code change must include appropriate unit tests
2. **Run Coverage Verification**: Execute `mvn verify` locally before submitting PRs to ensure coverage thresholds are met
3. **Address Coverage Gaps**: If coverage drops below 80%, add tests for:
   - Uncovered lines in new methods
   - Missing branch conditions (if/else, switch cases)
   - Exception handling paths
   - Edge cases and boundary conditions

### Writing Tests

When adding or modifying code, include tests that:

1. **Unit Tests**: Test individual methods and classes in isolation
   - Use `@Test` annotation from JUnit 5
   - Mock dependencies using `@MockBean` or Mockito
   - Follow the Arrange-Act-Assert pattern
   - Place tests in `src/test/java` mirroring the source structure

2. **Integration Tests**: Test component interactions when needed
   - Use `@SpringBootTest` for full application context
   - Use `@WebMvcTest` for controller layer testing
   - Use `@DataJpaTest` for repository layer testing

3. **Test Naming**: Use descriptive test method names
   - Pattern: `methodName_scenario_expectedResult`
   - Example: `createSample_withValidData_returnsCreatedSample`

### SonarQube Quality Gates

The CI/CD pipeline runs SonarQube analysis. Ensure your changes:

- **Maintain or improve code coverage** (target: ≥80%)
- **Resolve all blocking issues** before merging
- **Minimize code duplication**
- **Fix security vulnerabilities** identified by SonarQube
- **Address code smells** when they impact maintainability

### Code Review Checklist

Before approving a PR, verify:

- [ ] All new/modified code has corresponding tests
- [ ] `mvn verify` passes locally with coverage requirements met
- [ ] SonarQube analysis shows no new blocking issues
- [ ] Test cases cover happy paths, edge cases, and error scenarios
- [ ] Tests are maintainable and follow project conventions
- [ ] No test files are disabled or marked with `@Disabled` without justification

### Running Tests Locally

```bash
# Run tests with coverage
mvn clean verify

# View coverage report
open target/site/jacoco/index.html

# Run SonarQube analysis locally (requires SONAR_TOKEN)
mvn verify sonar:sonar -Dsonar.projectKey=your-project-key -Dsonar.host.url=your-sonar-url
```

### Common Testing Patterns in This Project

- **Controller Tests**: Use `MockMvc` to test REST endpoints
- **Service Tests**: Mock repository dependencies
- **Model Tests**: Test entity validation and business logic
- **Exception Tests**: Verify proper error handling and responses

### Coverage Exemptions

Some code may be excluded from coverage requirements:
- Configuration classes (if business logic-free)
- DTOs with only getters/setters (prefer records or Lombok)
- Main application class
- Generated code

If you need to exclude code from coverage, document the reason and use JaCoCo exclusion annotations sparingly.

## Project-Specific Guidelines

- **Framework**: Spring Boot 4.0.1 with Java 21
- **Testing**: JUnit 5, Spring Boot Test, Mockito
- **Coverage Tool**: JaCoCo 0.8.12
- **Code Analysis**: SonarQube integrated in CI/CD
- **Build Command**: `mvn verify` (includes tests and coverage check)
- **CI/CD**: GitHub Actions enforces coverage and quality gates

## Additional Resources

- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [SonarQube Quality Gates](https://docs.sonarqube.org/latest/user-guide/quality-gates/)
- [Spring Boot Testing Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
