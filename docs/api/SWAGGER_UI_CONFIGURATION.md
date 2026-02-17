# Swagger UI Configuration

This document describes the Swagger UI (OpenAPI) configuration for the starter-api-spring-mysql project.

## Overview

The project uses **SpringDoc OpenAPI 3.0.1** to automatically generate API documentation and provide
an interactive Swagger UI interface. All REST controllers are documented using OpenAPI annotations.

## Accessing Swagger UI

### Local Development

When running the application locally, Swagger UI is available at:

- **Swagger UI**: <http://localhost:8080/swagger-ui/index.html>
- **Alternative URL**: <http://localhost:8080/swagger-ui.html> (redirects to index.html)
- **OpenAPI JSON**: <http://localhost:8080/api-docs>
- **OpenAPI YAML**: <http://localhost:8080/api-docs.yaml>

### Docker Deployment

When deployed via docker-compose, Swagger UI is available at:

- **Swagger UI**: `http://<your-domain>/swagger-ui/index.html`
- **Alternative URL**: `http://<your-domain>/swagger-ui.html`
- **OpenAPI JSON**: `http://<your-domain>/api-docs`

## Configuration

### application.yml

The main configuration is located in `src/main/resources/application.yml`:

```yaml
springdoc:
  api-docs:
    path: ${SPRINGDOC_API_DOCS_PATH:/api-docs}
  swagger-ui:
    path: ${SPRINGDOC_SWAGGER_UI_PATH:/swagger-ui.html}
    enabled: ${SPRINGDOC_SWAGGER_UI_ENABLED:true}
  show-actuator: ${SPRINGDOC_SHOW_ACTUATOR:true}
```

### Environment Variables

All configuration values can be overridden using environment variables:

| Variable                       | Default Value      | Description                                        |
|--------------------------------|--------------------|--------------------------------------------------- |
| `SPRINGDOC_API_DOCS_PATH`      | `/api-docs`        | Path for OpenAPI JSON documentation                |
| `SPRINGDOC_SWAGGER_UI_PATH`    | `/swagger-ui.html` | Path for Swagger UI interface                      |
| `SPRINGDOC_SWAGGER_UI_ENABLED` | `true`             | Enable/disable Swagger UI                          |
| `SPRINGDOC_SHOW_ACTUATOR`      | `true`             | Show Spring Boot Actuator endpoints in Swagger UI  |

### docker-compose.yml

The Docker Compose configuration includes these environment variables for the API service:

```yaml
environment:
  SPRINGDOC_API_DOCS_PATH: ${SPRINGDOC_API_DOCS_PATH:-/api-docs}
  SPRINGDOC_SWAGGER_UI_PATH: ${SPRINGDOC_SWAGGER_UI_PATH:-/swagger-ui.html}
  SPRINGDOC_SWAGGER_UI_ENABLED: ${SPRINGDOC_SWAGGER_UI_ENABLED:-true}
  SPRINGDOC_SHOW_ACTUATOR: ${SPRINGDOC_SHOW_ACTUATOR:-true}
```

### .env File

For local development, you can set these variables in your `.env` file:

```bash
# SPRINGDOC / SWAGGER
SPRINGDOC_API_DOCS_PATH=/api-docs
SPRINGDOC_SWAGGER_UI_PATH=/swagger-ui.html
SPRINGDOC_SWAGGER_UI_ENABLED=true
SPRINGDOC_SHOW_ACTUATOR=true
```

## Disabling Swagger UI in Production

To disable Swagger UI in production environments, set the environment variable:

```bash
SPRINGDOC_SWAGGER_UI_ENABLED=false
```

Or in `docker-compose.yml`:

```yaml
environment:
  SPRINGDOC_SWAGGER_UI_ENABLED: false
```

## Customizing API Documentation

### Controller-Level Documentation

Controllers use OpenAPI annotations to provide rich documentation:

```java
@RestController
@Tag(name = "sample", description = "CRUD operations for Sample entities")
public class SampleController {
    
    @PostMapping("sample")
    @Operation(summary = "Create a new sample", description = "Creates a new sample with validation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sample created successfully",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Sample.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed",
            content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Sample createSample(@RequestBody SampleCreateForm form) {
        return this.sampleCreateService.create(form);
    }
}
```

### Available Annotations

- `@Tag`: Groups endpoints under a category
- `@Operation`: Describes an endpoint operation
- `@ApiResponses`: Documents possible HTTP responses
- `@ApiResponse`: Documents a specific response status
- `@Schema`: Provides schema information for request/response models
- `@Parameter`: Documents request parameters

## Troubleshooting

### Swagger UI Returns 404

If Swagger UI is not accessible:

1. **Check configuration**: Ensure `springdoc` configuration is present in `application.yml`
2. **Check environment variables**: Verify that `SPRINGDOC_SWAGGER_UI_ENABLED` is set to `true`
3. **Check dependency**: Ensure `springdoc-openapi-starter-webmvc-ui` is in `pom.xml`
4. **Check application logs**: Look for SpringDoc initialization messages
5. **Restart application**: Configuration changes require application restart

### OpenAPI JSON Not Generated

If `/api-docs` endpoint returns 404:

1. Check that `SPRINGDOC_API_DOCS_PATH` is configured correctly
2. Verify controllers have proper OpenAPI annotations
3. Check application logs for SpringDoc errors

### Actuator Endpoints Not Visible

If actuator endpoints don't appear in Swagger UI:

1. Verify `SPRINGDOC_SHOW_ACTUATOR=true`
2. Ensure actuator endpoints are exposed in `application.yml`:

   ```yaml
   management:
     endpoints:
       web:
         exposure:
           include: health,info,metrics
   ```

## Testing

The project includes integration tests to verify Swagger UI accessibility:

- **Test class**: `fr.tiogars.starter.config.SwaggerUITest`
- **Tests**:
  - `shouldAccessSwaggerUIIndexPage()`: Verifies `/swagger-ui/index.html` is accessible
  - `shouldAccessSwaggerUIHtmlPage()`: Verifies `/swagger-ui.html` redirects properly
  - `shouldAccessApiDocs()`: Verifies `/api-docs` returns OpenAPI JSON

Run tests with:

```bash
mvn test -Dtest=SwaggerUITest
```

## References

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [SpringDoc Configuration Properties](https://springdoc.org/properties.html)
- [OpenAPI 3 Specification](https://swagger.io/specification/)
- [Swagger UI Documentation](https://swagger.io/tools/swagger-ui/)

## Related Documentation

- [API Documentation Index](index.md)
- [Docker Deployment Guide](../setup/DOCKER_DEPLOYMENT.md)
- [Testing Documentation](../testing/index.md)
