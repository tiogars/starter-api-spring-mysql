package fr.tiogars.starter.exceptions;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Error response structure for API validation errors.
 * Used to document error responses in Swagger/OpenAPI.
 */
@Schema(description = "Error response for validation failures")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error type", example = "Validation Failed")
    private String error;

    @Schema(description = "Human-readable error message", example = "Les données fournies ne sont pas valides")
    private String message;

    @Schema(description = "Map of field names to their specific validation error messages")
    private Map<String, String> violations;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String error, String message, Map<String, String> violations) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.violations = violations;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getViolations() {
        return violations;
    }

    public void setViolations(Map<String, String> violations) {
        this.violations = violations;
    }
}
