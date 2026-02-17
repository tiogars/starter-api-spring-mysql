# Spring Boot 4 Migration Notes

This document tracks changes required for Spring Boot 4 compatibility in the starter-api-spring-mysql project.

## Package Structure Changes

### Test Autoconfiguration Annotations

In Spring Boot 4.0, several test-related annotations have been moved to new packages due to the modularization
of testing dependencies.

#### @AutoConfigureMockMvc

- **Previous Location (Spring Boot 2.x/3.x):**
  `org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc`
- **New Location (Spring Boot 4.x):**
  `org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc`

**Required Dependency:**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc-test</artifactId>
    <scope>test</scope>
</dependency>
```

**Usage Example:**

```java
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // test methods...
}
```

#### @WebMvcTest

- **Previous Location (Spring Boot 2.x/3.x):**
  `org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest`
- **New Location (Spring Boot 4.x):**
  `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`

## Files Updated

### Test Files

1. **src/test/java/fr/tiogars/starter/config/SwaggerUITest.java**
   - Updated `@AutoConfigureMockMvc` import from old package to new Spring Boot 4 package
   - Commit: Fix: Update @AutoConfigureMockMvc import for Spring Boot 4 compatibility

## Migration Checklist

When upgrading to Spring Boot 4, review and update:

- [ ] All test files using `@AutoConfigureMockMvc` - update import path
- [ ] All test files using `@WebMvcTest` - update import path
- [ ] Verify `spring-boot-starter-webmvc-test` dependency is included in pom.xml
- [ ] Run full test suite to verify all tests still pass
- [ ] Update any custom testing utilities that reference old package paths

## References

- [Spring Boot 4 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide)
- [Spring Boot 4 Testing Changes](https://rieckpil.de/whats-new-for-testing-in-spring-boot-4-0-and-spring-framework-7/)
- [GitHub Issue: Missed AutoConfigureMockMvc in spring-boot-test-autoconfigure-4.0.0]
  (<https://github.com/spring-projects/spring-boot/issues/48286>)

## Version Information

- **Spring Boot Version:** 4.0.2
- **Java Version:** 21
- **Migration Date:** February 17, 2026
