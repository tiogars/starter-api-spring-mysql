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
  - Test successful responses (200, 201)
  - Test error responses (404, 400, etc.)
  - Test with valid and invalid input data
  - Test edge cases (empty lists, null values)
- **Service Tests**: Mock repository dependencies
  - Test all conditional branches (if/else, switch cases)
  - Test error handling and exception paths
  - Test null checks and empty collection handling
  - Test business logic with different input combinations
- **Model Tests**: Test entity validation and business logic
- **Exception Tests**: Verify proper error handling and responses

### Branch Coverage Best Practices

To achieve and maintain 80% branch coverage:

1. **Test All Conditional Paths**

   ```java
   // If you have code like:
   if (value != null) {
       // path A
   } else {
       // path B
   }
   
   // Write tests for both paths:
   @Test void testWithNullValue() { }  // Tests path B
   @Test void testWithNonNullValue() { }  // Tests path A
   ```

2. **Test Switch Cases**
   - Create a test for each case statement
   - Include a test for the default case

3. **Test Exception Handling**
   - Test the happy path (no exceptions)
   - Test when each type of exception is thrown
   - Verify recovery logic works correctly

4. **Test Loop Conditions**
   - Test with empty collections
   - Test with single-item collections
   - Test with multiple items

5. **Test Boolean Logic**
   - For `value && otherValue`, test: (true, true), (true, false), (false, true), (false, false)
   - For `value || otherValue`, test: (true, *), (false, true), (false, false)

### Examples of Branch Coverage Tests

#### Example 1: Testing Operator Branches

```java
// Service code with multiple operators
public Result process(String operator, int value) {
    switch (operator) {
        case "equals": return checkEquals(value);
        case "greater": return checkGreater(value);
        case "less": return checkLess(value);
        default: throw new IllegalArgumentException();
    }
}

// Tests covering all branches
@Test void testProcessWithEquals() { }
@Test void testProcessWithGreater() { }
@Test void testProcessWithLess() { }
@Test void testProcessWithInvalidOperator() { }
```

#### Example 2: Testing Error Combinations

```java
// Import service with multiple error types
public Report importItems(List<Item> items) {
    int created = 0, duplicates = 0, errors = 0;
    for (Item item : items) {
        if (exists(item)) {
            duplicates++;
        } else if (!validate(item)) {
            errors++;
        } else {
            created++;
        }
    }
    return new Report(created, duplicates, errors);
}

// Tests covering different combinations
@Test void testImportAllNew() { }
@Test void testImportAllDuplicates() { }
@Test void testImportAllErrors() { }
@Test void testImportMixedResults() { }
@Test void testImportEmptyList() { }
```

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
- **Coverage Tool**: JaCoCo 0.8.14
- **Code Analysis**: SonarQube integrated in CI/CD
- **Build Command**: `mvn verify` (includes tests and coverage check)
- **CI/CD**: GitHub Actions enforces coverage and quality gates

## Documentation Organization

### Markdown File Management

When generating or creating Markdown documentation files:

1. **Organize by Theme**: Place generated Markdown files in the `docs/` folder, organized into thematic subdirectories:
   - `docs/testing/` - Testing guides, coverage reports, test organization
   - `docs/api/` - API documentation, endpoint guides, usage examples
   - `docs/implementation/` - Implementation details, completion reports, technical documentation
   - `docs/features/` - Feature documentation and user guides

2. **Create Index Files**: Each thematic folder should have an `index.md` file that:
   - Provides an overview of the theme
   - Lists and links to all documents in that folder
   - Follows a consistent structure across themes

3. **Update Main Index**: Keep the root `docs/README.md` (or create one if it doesn't exist)
   updated with:

   - Links to all thematic `index.md` files
   - Brief description of each theme
   - Quick navigation to commonly accessed documents

4. **File Naming Conventions**:
   - Use clear, descriptive names with underscores or hyphens
   - Use UPPERCASE for generated reports (e.g., `TESTING_SUMMARY.md`)
   - Use lowercase for user guides (e.g., `getting-started.md`)

5. **Keep Root Clean**: Only essential files like `README.md` should remain at the
   repository root. Move all other documentation to the appropriate `docs/` subdirectory.

## Markdown Documentation Quality

### MarkdownLint Requirements

When creating or modifying Markdown documentation files:

1. **Run MarkdownLint Before Committing**: Always run MarkdownLint to check for errors
   and warnings in Markdown files

   ```bash
   # Check all markdown files
   npx markdownlint-cli "**/*.md" --ignore node_modules
   
   # Check specific file
   npx markdownlint-cli path/to/file.md
   
   # Fix automatically fixable issues
   npx markdownlint-cli "**/*.md" --fix --ignore node_modules
   ```

2. **MarkdownLint Configuration**: The project uses `.markdownlint.json` configuration with:
   - Maximum line length: 120 characters (MD013)
   - All default rules enabled
   - HTML elements allowed: `<a>`, `<br>`, `<img>` (MD033)
   - First line heading requirement disabled (MD041)

3. **Common MarkdownLint Rules to Follow**:
   - **MD001**: Heading levels should only increment by one level at a time
   - **MD012**: No multiple consecutive blank lines
   - **MD013**: Line length should not exceed 120 characters
   - **MD022**: Headings should be surrounded by blank lines
   - **MD031**: Fenced code blocks should be surrounded by blank lines
   - **MD032**: Lists should be surrounded by blank lines
   - **MD040**: Fenced code blocks should have a language specified (e.g., bash, json, java)
   - **MD060**: Table columns should have consistent spacing

4. **Best Practices**:

   - Always specify language for code blocks (e.g., bash, java, json)
   - Keep lines under 120 characters when possible
   - Add blank lines before and after headings, lists, and code blocks
   - Use consistent table formatting with proper spacing around pipes
   - Break long lines in tables or use reference-style links

5. **Workflow**:
   - Create/modify Markdown files
   - Run `npx markdownlint-cli "**/*.md" --fix --ignore node_modules` to auto-fix simple issues
   - Manually fix remaining errors reported by MarkdownLint
   - Verify no errors remain before committing
   - Include MarkdownLint results in commit message if significant changes were made

6. **Handling Line Length Issues**:
   - For long URLs, use reference-style links: `[text][ref]` and define `[ref]: url` at the bottom
   - For long table cells, abbreviate or split into multiple rows
   - For long code lines, this is acceptable as code blocks are excluded from strict enforcement

### Integration with CI/CD

While MarkdownLint is not currently enforced in CI/CD, running it locally ensures:

- Consistent documentation quality
- Better readability
- Compliance with Markdown best practices
- Easier maintenance and collaboration

## Architecture Guidelines - IT Architect Role

When implementing new features or modifying existing code, **act as an IT Architect** to ensure:

### Single Responsibility Principle (SRP)

**Domain services MUST follow SRP to remain readable and maintainable.**

1. **Separate Concerns**: Each service class should have ONE clear responsibility
   - ✅ **Good**: `SampleCreateService` - handles only sample creation logic
   - ✅ **Good**: `SampleSearchService` - handles only search/filtering logic
   - ✅ **Good**: `SampleImportService` - handles only bulk import operations
   - ❌ **Bad**: `SampleService` - mixing create, search, import, export, and update logic

2. **Service Naming Convention**: Use descriptive names that reflect the single responsibility
   - `{Domain}CreateService` - Creation operations
   - `{Domain}FindService` - Read operations (findAll, findById)
   - `{Domain}UpdateService` - Update operations
   - `{Domain}SearchService` - Complex search/filtering operations
   - `{Domain}ImportService` - Bulk import operations
   - `{Domain}ExportService` - Export operations
   - `{Domain}CrudService` - Only if truly handling all basic CRUD (use sparingly)

3. **Keep Services Focused**:
   - Each service should be easy to understand at a glance
   - Limit service size (aim for < 300 lines per service)
   - If a service grows too large, split it based on responsibilities

### Abstract Services - Reuse and Extension

**Abstract services MUST be used when adding new data domains to maintain consistency and reduce duplication.**

#### Available Abstract Services

This project uses external architecture packages that provide abstract services:

1. **AbstractCreateService** (from `architecture-create-service` package)
   - Provides standardized creation logic
   - Handles form-to-model-to-entity conversion
   - Includes validation hooks
   - **Usage**: Extend for all domain creation services

2. **AbstractFindService** (from `fr.tiogars.starter.common.services`)
   - Provides read-only find operations (findAll, findById)
   - Enforces entity-to-model mapping pattern
   - **Usage**: Extend for all domain find services

#### When Adding a New Data Domain

Follow this pattern when adding a new domain (e.g., "Product", "Order"):

1. **Create Find Service** extending `AbstractFindService`:

   ```java
   @Service
   public class ProductFindService extends AbstractFindService<ProductEntity, Long, Product> {
       public ProductFindService(ProductRepository repository) {
           super(repository);
       }
       
       @Override
       protected Product toModel(ProductEntity entity) {
           // Implement entity-to-model mapping
       }
   }
   ```

2. **Create Create Service** extending `AbstractCreateService`:

   ```java
   @Service
   @Validated
   public class ProductCreateService 
           extends AbstractCreateService<ProductCreateForm, ProductEntity, Product, ProductRepository> {
       
       private final Validator validator;
       
       public ProductCreateService(ProductRepository repository, Validator validator) {
           super(repository);
           this.validator = validator;
       }
       
       @Override
       public ProductEntity toEntity(Product model) {
           // Implement model-to-entity conversion
       }
       
       @Override
       public Product toModel(ProductEntity entity) {
           // Implement entity-to-model conversion
       }
       
       @Override
       public Product toModel(ProductCreateForm form) {
           // Implement form-to-model conversion
       }
       
       @Override
       public void validate(Product model) {
           Set<ConstraintViolation<Product>> violations = validator.validate(model);
           if (!violations.isEmpty()) {
               throw new ConstraintViolationException(violations);
           }
       }
   }
   ```

3. **Create Specialized Services** as needed:
   - `ProductSearchService` - for complex filtering/pagination (see `SampleSearchService` example)
   - `ProductImportService` - for bulk operations (see `SampleImportService` example)
   - `ProductUpdateService` - for update-specific logic
   - `ProductExportService` - for export functionality

#### Benefits of Using Abstract Services

- **Consistency**: All domains follow the same patterns
- **Reduced Duplication**: Common CRUD logic is centralized
- **Easier Testing**: Standard patterns make tests predictable
- **Maintainability**: Changes to base services propagate to all domains
- **Code Quality**: Enforces separation of concerns

### Reviewing and Improving Abstract Services

**If you encounter difficulties implementing abstract services:**

1. **First Check**: Review existing implementations in the codebase
   - Look at `SampleCreateService`, `FeatureCreateService` for create patterns
   - Look at `SampleFindService` (in `fr.tiogars.starter.sample.services`) for find patterns
   - Look at `SampleSearchService` for search patterns

2. **Identify the Issue**:
   - Is the abstract service missing a needed method?
   - Is the generic signature too restrictive?
   - Is there a use case not covered by the abstraction?

3. **Propose Improvements**:
   - Document the limitation in code review comments
   - Suggest specific enhancements to abstract services
   - Consider if the issue is domain-specific or affects all domains
   - If it's a common need, propose updating the abstract service
   - If it's domain-specific, implement it in the domain service

4. **Do NOT Work Around Abstract Services**:
   - ❌ Don't duplicate logic that should be in abstract services
   - ❌ Don't bypass abstract services with custom implementations
   - ✅ Extend and enhance abstract services when needed
   - ✅ Follow the established patterns even for edge cases

### Architecture Review Checklist

Before approving any PR that adds or modifies services:

- [ ] Each service has a single, clear responsibility
- [ ] Service names accurately reflect their purpose
- [ ] New domains extend appropriate abstract services
- [ ] No duplication of logic available in abstract services
- [ ] Entity-to-model and model-to-entity conversions are properly implemented
- [ ] Validation logic is present where needed
- [ ] Related domains (tags, apps, repositories) are properly handled
- [ ] Service classes are reasonably sized (< 300 lines preferred)
- [ ] Code follows existing patterns in similar services

### Examples from This Codebase

**Good Architecture Examples:**

1. **Sample Domain** - Well-structured with SRP:
   - `SampleFindService` - extends `AbstractFindService` for reads
   - `SampleCreateService` - extends `AbstractCreateService` for creation
   - `SampleUpdateService` - handles updates
   - `SampleSearchService` - complex filtering and pagination
   - `SampleImportService` - bulk import with duplicate checking
   - `SampleExportService` - export functionality

2. **Feature Domain** - Follows same pattern:
   - `FeatureFindService` - extends `AbstractFindService`
   - `FeatureCreateService` - extends `AbstractCreateService`
   - `FeatureImportService` - bulk operations

**Anti-patterns to Avoid:**

1. **God Services**: A single `ProductService` with create, read, update, delete, search, import, export
2. **Ignoring Abstractions**: Implementing custom CRUD logic instead of extending abstract services
3. **Mixed Concerns**: Putting business logic and data access in the same service
4. **Inconsistent Patterns**: Each domain using different structures

## Additional Resources

- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [SonarQube Quality Gates](https://docs.sonarqube.org/latest/user-guide/quality-gates/)
- [Spring Boot Testing Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [MarkdownLint Rules](https://github.com/DavidAnson/markdownlint/blob/main/doc/Rules.md)
- [SOLID Principles](https://en.wikipedia.org/wiki/SOLID)
- [Single Responsibility Principle](https://en.wikipedia.org/wiki/Single-responsibility_principle)
