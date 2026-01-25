# Maven Site Reports

## Overview

This project uses Maven Site Plugin to generate comprehensive project documentation and reports. The site includes multiple reports that provide insights into the project's health, dependencies, test coverage, and more.

## Available Reports

### 1. JaCoCo Coverage Report

**Location**: `target/site/jacoco/index.html` (after running `mvn test` or `mvn verify`)  
**Site Location**: `target/site/jacoco/index.html` (after running `mvn site`)

The JaCoCo (Java Code Coverage) report provides detailed code coverage metrics including:

- **Line Coverage**: Percentage of code lines executed by tests
- **Branch Coverage**: Percentage of conditional branches (if/else, switch) covered
- **Instruction Coverage**: Percentage of bytecode instructions executed
- **Complexity Coverage**: Cyclomatic complexity metrics

**Coverage Thresholds (Enforced)**:
- Line Coverage: ≥80%
- Branch Coverage: ≥80%
- Instruction Coverage: ≥80%

These thresholds are enforced during the `mvn verify` phase. The build will fail if coverage falls below these levels.

### 2. Project Information Reports

The Maven Project Info Reports plugin provides the following reports:

- **Index**: Overview of the project
- **Summary**: Project summary including name, version, and description
- **Dependency Info**: How to include this project as a dependency
- **Dependencies**: List of project dependencies with versions
- **Licenses**: License information for the project
- **Team**: Development team information
- **SCM**: Source code management information (Git repository)

## Generating Reports

### Generate All Reports

To generate the complete Maven site with all reports:

```bash
mvn clean site
```

This command will:
1. Clean the previous build (`clean`)
2. Run tests to collect coverage data
3. Generate all configured reports
4. Create the site in `target/site/`

### Generate Only JaCoCo Report

If you only need the JaCoCo coverage report:

```bash
mvn clean test
```

The report will be available at `target/site/jacoco/index.html`

### Verify Coverage Thresholds

To run tests and verify that coverage meets the required thresholds:

```bash
mvn clean verify
```

This will:
1. Run all tests
2. Generate coverage reports
3. Check that coverage meets 80% thresholds
4. Fail the build if thresholds are not met

## Viewing Reports Locally

After generating the site:

1. **Open in Browser**:
   ```bash
   # On Linux/Mac
   open target/site/index.html
   
   # On Windows
   start target/site/index.html
   ```

2. **Navigate to JaCoCo Report**:
   - From the main site page, click on "Project Reports" > "JaCoCo"
   - Or directly open: `target/site/jacoco/index.html`

## CI/CD Integration

### GitHub Actions Workflow

The project includes a dedicated workflow (`.github/workflows/maven-site.yml`) that:

1. Builds the Maven site with all reports
2. Builds the MkDocs documentation
3. Combines both into a single deployment package
4. Deploys to GitHub Pages

**Trigger Events**:
- Push to `main` branch
- Pull requests to `main` branch
- Manual workflow dispatch

### Viewing Reports on GitHub Pages

After deployment, reports are available at:

- **MkDocs Documentation**: `https://<username>.github.io/<repository>/`
- **Maven Site**: `https://<username>.github.io/<repository>/maven/`
- **JaCoCo Report**: `https://<username>.github.io/<repository>/maven/jacoco/`

## Configuration

### POM Configuration

The Maven site and JaCoCo reporting are configured in `pom.xml`:

**Build Plugins** (for coverage collection):
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.14</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>check</id>
            <phase>verify</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Reporting Plugins** (for site generation):
```xml
<reporting>
    <plugins>
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.14</version>
            <reportSets>
                <reportSet>
                    <reports>
                        <report>report</report>
                    </reports>
                </reportSet>
            </reportSets>
        </plugin>
    </plugins>
</reporting>
```

## Understanding JaCoCo Report

### Coverage Metrics

The JaCoCo report provides several coverage metrics:

1. **Instructions (C0 Coverage)**
   - Smallest unit of code
   - Individual Java bytecode instructions
   - Most granular coverage metric

2. **Branches (C1 Coverage)**
   - Decision points in code (if/else, switch, loops)
   - Measures whether all possible paths are tested
   - Critical for testing edge cases

3. **Lines**
   - Source code lines that contain at least one instruction
   - Most commonly referenced metric

4. **Cyclomatic Complexity**
   - Number of independent paths through code
   - Higher complexity = more test cases needed

5. **Methods**
   - Coverage of individual methods
   - Helps identify untested methods

6. **Classes**
   - Coverage at the class level
   - Overview of which classes need more tests

### Color Coding

- **Green**: Fully covered (100%)
- **Yellow**: Partially covered
- **Red**: Not covered (0%)

### Drilling Down

The JaCoCo report is interactive:
1. Start at package level
2. Click package to see classes
3. Click class to see methods
4. Click method to see line-by-line coverage

Uncovered lines are highlighted in red, partially covered in yellow.

## Best Practices

### Regular Coverage Checks

1. **Before Committing**:
   ```bash
   mvn clean verify
   ```
   Ensures your changes meet coverage thresholds

2. **After Adding Features**:
   ```bash
   mvn clean test
   open target/site/jacoco/index.html
   ```
   Review coverage to identify untested code

3. **Before Pull Requests**:
   ```bash
   mvn clean site
   ```
   Generate full site to review all reports

### Coverage Improvement Workflow

1. Run tests with coverage: `mvn clean verify`
2. Review JaCoCo report: `target/site/jacoco/index.html`
3. Identify uncovered lines (red) and branches (yellow)
4. Write tests for uncovered code
5. Re-run coverage check
6. Iterate until thresholds are met

### Exclusions

Some code may not require coverage:
- Configuration classes with no business logic
- Data transfer objects (DTOs) with only getters/setters
- Main application class
- Generated code

However, this project maintains strict 80% thresholds, so exclusions should be rare and well-justified.

## Troubleshooting

### Build Fails: Coverage Below Threshold

**Error**:
```
[ERROR] Rule violated for bundle starter: instructions covered ratio is 0.75, but expected minimum is 0.80
```

**Solution**:
1. Run `mvn clean test`
2. Open `target/site/jacoco/index.html`
3. Identify uncovered code
4. Add tests to cover the gaps
5. Run `mvn verify` to confirm

### Site Generation Fails

**Error**: Dependencies cannot be resolved

**Solution**:
Ensure GitHub packages authentication is configured:
```bash
export GITHUB_ACTOR=<your-github-username>
export GITHUB_TOKEN=<your-github-token>
mvn clean site
```

### JaCoCo Report Not in Site

**Check**:
1. Verify `<reporting>` section in `pom.xml` includes JaCoCo plugin
2. Run `mvn clean site` (not just `mvn site`)
3. Check `target/site/project-reports.html` for JaCoCo link

## Additional Resources

- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [Maven Site Plugin](https://maven.apache.org/plugins/maven-site-plugin/)
- [Maven Project Info Reports](https://maven.apache.org/plugins/maven-project-info-reports-plugin/)
- [Test Coverage Guide](TEST_COVERAGE_GUIDE.md)
- [Branch Coverage Summary](BRANCH_COVERAGE_SUMMARY.md)

## Summary

Maven site reports, particularly JaCoCo coverage reports, are essential tools for maintaining code quality. By regularly reviewing these reports and maintaining the 80% coverage thresholds, we ensure:

- High-quality, well-tested code
- Early detection of untested code paths
- Confidence in refactoring and changes
- Documentation of project health and dependencies

Make it a habit to generate and review these reports as part of your development workflow.
