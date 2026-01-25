# Mockito Agent Configuration

## Overview

This document explains the configuration changes made to resolve Mockito self-attachment warnings that appear during test execution.

## Problem

When running tests with Mockito on Java 21, the following warnings were displayed:

```
WARNING: Mockito is currently self-attaching to enable the inline-mock-maker. 
This will no longer work in future releases of the JDK.
Please add Mockito as an agent to your build as described in Mockito's documentation:
https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3

WARNING: A Java agent has been loaded dynamically (byte-buddy-agent-1.17.8.jar)
WARNING: If a serviceability tool is in use, please run with -XX:+EnableDynamicAgentLoading to hide this warning
WARNING: If a serviceability tool is not in use, please run with -Djdk.instrument.traceUsage for more information
WARNING: Dynamic loading of agents will be disallowed by default in a future release
```

These warnings indicate that Mockito is using dynamic self-attachment to enable its inline mock maker, which will not be supported in future JDK releases.

## Solution

The solution is to configure Mockito's byte-buddy-agent as a Java agent at JVM startup, rather than relying on dynamic self-attachment.

### Changes Made

#### 1. Added byte-buddy-agent Dependency

Added an explicit test-scoped dependency for `byte-buddy-agent` in `pom.xml`:

```xml
<!-- https://mvnrepository.com/artifact/net.bytebuddy/byte-buddy-agent -->
<dependency>
    <groupId>net.bytebuddy</groupId>
    <artifactId>byte-buddy-agent</artifactId>
    <scope>test</scope>
</dependency>
```

This dependency is automatically managed by Spring Boot's dependency management, which sets the version to `${byte-buddy.version}` (currently 1.17.8).

#### 2. Configured Maven Surefire Plugin

Added configuration to the `maven-surefire-plugin` to load the byte-buddy-agent as a Java agent:

```xml
<!-- https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-surefire-plugin -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <argLine>@{argLine} -javaagent:${settings.localRepository}/net/bytebuddy/byte-buddy-agent/${byte-buddy.version}/byte-buddy-agent-${byte-buddy.version}.jar</argLine>
    </configuration>
</plugin>
```

**Key Points:**
- `@{argLine}` preserves any existing JVM arguments (e.g., from JaCoCo)
- `-javaagent:` specifies the path to the byte-buddy-agent JAR
- `${settings.localRepository}` resolves to the Maven local repository (typically `~/.m2/repository`)
- `${byte-buddy.version}` uses the version managed by Spring Boot parent POM

## Verification

### Local Testing

To verify the configuration works correctly, run the tests locally:

```bash
mvn clean test
```

The Mockito warnings should no longer appear in the console output.

### CI/CD Pipeline

The configuration is automatically applied when tests run in the GitHub Actions workflow:

```bash
mvn -B verify
```

## Technical Details

### How It Works

1. **At Build Time**: Maven downloads the `byte-buddy-agent` JAR to the local repository
2. **At Test Time**: The Surefire plugin starts the JVM with the `-javaagent` argument
3. **During Tests**: Mockito detects the agent is already loaded and skips self-attachment
4. **Result**: No warnings are displayed, and the configuration is future-proof

### Compatibility

- **Java Version**: Java 21 (current project version)
- **Spring Boot**: 4.0.2
- **Mockito**: 5.20.0 (managed by Spring Boot)
- **Byte Buddy**: 1.17.8 (managed by Spring Boot)

## References

- [Mockito Documentation - Java Agents](https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3)
- [Maven Surefire Plugin Documentation](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [Byte Buddy Agent Documentation](https://bytebuddy.net/)

## Related Issues

- GitHub Issue: "Mockito as an agent"
- Root Cause: Dynamic agent loading will be disallowed in future JDK releases
- Solution: Pre-load byte-buddy-agent via Maven Surefire plugin configuration
