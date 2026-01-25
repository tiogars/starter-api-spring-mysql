# Branch Coverage Test Guide

## Overview

This document provides guidance for achieving and maintaining 80% branch coverage in the starter-api-spring-mysql project.

## Branch Coverage Basics

**Branch coverage** measures whether each possible branch (true/false outcomes) of every decision point has been executed during testing. This includes:

- if/else statements
- switch/case statements
- ternary operators (? :)
- short-circuit boolean operators (&& and ||)
- try/catch blocks
- Loop conditions

## Current Status

As of the latest updates, the project has added 36+ new comprehensive branch coverage tests covering:

- ✅ LegacyTagController (7 tests)
- ✅ SampleExportService (9 additional tests)
- ✅ SampleImportService (10 additional tests)
- ✅ FeatureImportService (10 additional tests)

## Target: 80% Branch Coverage

The project enforces an 80% minimum for:
- Line coverage
- **Branch coverage** ← Primary focus
- Instruction coverage

### Why 80%?

- Industry standard for production code
- Required by SonarQube quality gates
- Ensures critical paths are tested
- Catches edge cases and error handling
- Improves code reliability and maintainability

## How to Check Coverage

### Run Tests with Coverage

```bash
# Clean build and run all tests with coverage
./mvnw clean verify

# View the JaCoCo HTML report
open target/site/jacoco/index.html
```

### In the JaCoCo Report

1. Look for the **"Branches"** column
2. Green = Good coverage (≥80%)
3. Yellow = Moderate coverage (50-79%)
4. Red = Poor coverage (<50%)

### Identifying Missing Branches

Click on a class in the JaCoCo report to see:
- Yellow highlighting = partially covered branches
- Red highlighting = uncovered lines
- Green highlighting = fully covered lines

## Writing Branch Coverage Tests

### Pattern 1: If/Else Statements

```java
// Source code
public String process(boolean condition) {
    if (condition) {
        return "success";  // Branch 1
    } else {
        return "failure";  // Branch 2
    }
}

// Tests - cover both branches
@Test
void testProcess_ConditionTrue_ReturnsSuccess() {
    String result = service.process(true);
    assertEquals("success", result);
}

@Test
void testProcess_ConditionFalse_ReturnsFailure() {
    String result = service.process(false);
    assertEquals("failure", result);
}
```

### Pattern 2: Switch Statements

```java
// Source code
public Result handleOperation(String operator, int value) {
    switch (operator) {
        case "add":
            return add(value);      // Branch 1
        case "subtract":
            return subtract(value); // Branch 2
        case "multiply":
            return multiply(value); // Branch 3
        default:
            throw new IllegalArgumentException("Unknown operator"); // Branch 4
    }
}

// Tests - one for each case
@Test void testHandleOperation_Add() { /* test add branch */ }
@Test void testHandleOperation_Subtract() { /* test subtract branch */ }
@Test void testHandleOperation_Multiply() { /* test multiply branch */ }
@Test void testHandleOperation_UnknownOperator() { /* test default branch */ }
```

### Pattern 3: Null Checks and Optional Handling

```java
// Source code
public String getName(Sample sample) {
    if (sample == null) {
        return "Unknown";  // Branch 1
    }
    return sample.getName();  // Branch 2
}

// Tests
@Test
void testGetName_NullSample_ReturnsUnknown() {
    String result = service.getName(null);
    assertEquals("Unknown", result);
}

@Test
void testGetName_ValidSample_ReturnsSampleName() {
    Sample sample = new Sample();
    sample.setName("Test");
    String result = service.getName(sample);
    assertEquals("Test", result);
}
```

### Pattern 4: Boolean Logic (&&, ||)

```java
// Source code
public boolean isValid(Sample sample) {
    if (sample != null && sample.getName() != null && sample.getName().length() > 0) {
        return true;
    }
    return false;
}

// Tests - test different combinations
@Test void testIsValid_NullSample_ReturnsFalse() { }
@Test void testIsValid_NullName_ReturnsFalse() { }
@Test void testIsValid_EmptyName_ReturnsFalse() { }
@Test void testIsValid_ValidName_ReturnsTrue() { }
```

### Pattern 5: Exception Handling

```java
// Source code
public Sample createSample(SampleForm form) {
    try {
        return repository.save(form);  // Branch 1: success
    } catch (ConstraintViolationException e) {
        logger.error("Validation failed", e);
        throw e;  // Branch 2: validation error
    } catch (Exception e) {
        logger.error("Unexpected error", e);
        return null;  // Branch 3: other errors
    }
}

// Tests
@Test void testCreateSample_Success() { /* test successful creation */ }
@Test void testCreateSample_ValidationError() { /* mock throws ConstraintViolationException */ }
@Test void testCreateSample_UnexpectedError() { /* mock throws RuntimeException */ }
```

### Pattern 6: Loop Conditions

```java
// Source code
public int countActive(List<Sample> samples) {
    int count = 0;
    for (Sample sample : samples) {
        if (sample.isActive()) {  // Branch in loop
            count++;
        }
    }
    return count;
}

// Tests
@Test void testCountActive_EmptyList_ReturnsZero() { }
@Test void testCountActive_AllInactive_ReturnsZero() { }
@Test void testCountActive_SomeActive_ReturnsCount() { }
@Test void testCountActive_AllActive_ReturnsAll() { }
```

## Common Areas Needing Branch Coverage

### 1. Controllers

- Test successful responses (200, 201, 204)
- Test error responses (400, 404, 500)
- Test with valid and invalid input
- Test with null/empty parameters

### 2. Services

- Test all operator branches (equals, greater than, etc.)
- Test error handling paths
- Test with null/empty inputs
- Test edge cases (boundaries, limits)

### 3. Import/Export Services

- Test different file formats
- Test empty data
- Test with and without compression
- Test invalid data handling

### 4. Form Validators

- Test valid data
- Test each validation rule separately
- Test combinations of validation failures

## Checklist for New Code

Before submitting a PR, ensure:

- [ ] Every if statement has tests for both true and false branches
- [ ] Every switch statement has tests for all cases including default
- [ ] Exception handling has tests for both success and error paths
- [ ] Null checks have tests for both null and non-null values
- [ ] Boolean logic (&&, ||) has tests for all combinations
- [ ] Loops have tests for empty, single item, and multiple items
- [ ] `mvn verify` passes with 80%+ branch coverage

## Troubleshooting Low Coverage

### Issue: Coverage Below 80%

1. Run `./mvnw clean verify`
2. Open `target/site/jacoco/index.html`
3. Find classes with yellow/red branch coverage
4. Click on the class to see uncovered branches (yellow/red highlighting)
5. Add tests for the missing branches
6. Re-run `./mvnw verify` to confirm improvement

### Issue: Can't Identify Missing Branches

Look for code patterns that create branches:
- `if`, `else if`, `else`
- `switch`, `case`, `default`
- `? :` ternary operators
- `&&` and `||` in conditions
- `try`, `catch`, `finally`
- Loop entry conditions

### Issue: Too Many Test Cases

Focus on:
1. **Critical paths first**: Main business logic
2. **Error paths second**: Exception handling
3. **Edge cases third**: Null, empty, boundaries

Don't test:
- Getters/setters without logic
- Auto-generated code
- Simple DTOs/models

## Tools and Commands

### Maven Commands

```bash
# Run tests and check coverage
./mvnw clean verify

# Run tests only (without coverage check)
./mvnw clean test

# Run a specific test class
./mvnw test -Dtest=SampleServiceTest

# Run a specific test method
./mvnw test -Dtest=SampleServiceTest#testMethod
```

### Viewing Reports

```bash
# JaCoCo HTML report
open target/site/jacoco/index.html

# JaCoCo XML report (for tools)
cat target/site/jacoco/jacoco.xml

# Surefire test results
open target/surefire-reports/index.html
```

## Examples from This Project

### Example 1: LegacyTagController

```java
@Test
void testGetTagById_Found() throws Exception {
    // Arrange
    when(tagService.findById(1L)).thenReturn(Optional.of(tag1));

    // Act & Assert
    mockMvc.perform(get("/sample-tag/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
}

@Test
void testGetTagById_NotFound() throws Exception {
    // Arrange
    when(tagService.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    mockMvc.perform(get("/sample-tag/999"))
        .andExpect(status().isNotFound());
}
```

**Branches covered**: Optional.isPresent() = true and false

### Example 2: SampleImportService

```java
@Test
void testImportSamples_AllNew_AllCreated() {
    // Tests: no duplicates, no errors
}

@Test
void testImportSamples_AllDuplicates_NoneCreated() {
    // Tests: all duplicates branch
}

@Test
void testImportSamples_ValidationFailure_ErrorReported() {
    // Tests: validation error branch
}

@Test
void testImportSamples_GenericException_ErrorReported() {
    // Tests: generic exception branch
}
```

**Branches covered**: All combinations in the import result classification logic

## Best Practices

1. **Write tests as you code**: Don't wait until the end
2. **Test one branch per test**: Keeps tests focused and clear
3. **Use descriptive test names**: Include the scenario and expected result
4. **Follow Arrange-Act-Assert**: Makes tests easy to read
5. **Mock external dependencies**: Keeps tests fast and isolated
6. **Test error paths**: Don't just test the happy path
7. **Use coverage reports**: Let JaCoCo guide you to missing branches

## Continuous Improvement

- Review coverage reports in every PR
- Add tests when coverage drops
- Refactor complex methods to make them more testable
- Share good testing patterns with the team
- Update this guide with new patterns as they emerge

## Resources

- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)

## Questions?

If you're unsure about how to test a specific branch or need help improving coverage:
1. Check this guide for similar patterns
2. Look at existing tests in the project for examples
3. Ask the team for guidance
4. Refer to the `.github/copilot-instructions.md` for AI-assisted help

---

**Remember**: The goal is not just to reach 80% coverage, but to write meaningful tests that actually verify the code works correctly!
