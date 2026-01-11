package fr.tiogars.starter.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ErrorResponseTest {

    @Test
    void testDefaultConstructor() {
        // Act
        ErrorResponse errorResponse = new ErrorResponse();

        // Assert
        assertNotNull(errorResponse);
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        int status = 400;
        String error = "Bad Request";
        String message = "Validation failed";
        Map<String, String> violations = new HashMap<>();
        violations.put("field1", "Error message 1");

        // Act
        ErrorResponse errorResponse = new ErrorResponse(status, error, message, violations);

        // Assert
        assertNotNull(errorResponse);
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(violations, errorResponse.getViolations());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        ErrorResponse errorResponse = new ErrorResponse();
        int status = 404;
        String error = "Not Found";
        String message = "Resource not found";
        Map<String, String> violations = new HashMap<>();
        violations.put("id", "ID not found");

        // Act
        errorResponse.setStatus(status);
        errorResponse.setError(error);
        errorResponse.setMessage(message);
        errorResponse.setViolations(violations);

        // Assert
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(violations, errorResponse.getViolations());
    }

    @Test
    void testWithNullViolations() {
        // Arrange & Act
        ErrorResponse errorResponse = new ErrorResponse(500, "Internal Server Error", "Something went wrong", null);

        // Assert
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("Something went wrong", errorResponse.getMessage());
        assertNull(errorResponse.getViolations());
    }

    @Test
    void testWithEmptyViolations() {
        // Arrange
        Map<String, String> emptyViolations = new HashMap<>();

        // Act
        ErrorResponse errorResponse = new ErrorResponse(400, "Bad Request", "Validation error", emptyViolations);

        // Assert
        assertNotNull(errorResponse.getViolations());
        assertTrue(errorResponse.getViolations().isEmpty());
    }

    @Test
    void testWithMultipleViolations() {
        // Arrange
        Map<String, String> violations = new HashMap<>();
        violations.put("field1", "Error 1");
        violations.put("field2", "Error 2");
        violations.put("field3", "Error 3");

        // Act
        ErrorResponse errorResponse = new ErrorResponse(400, "Validation Failed", "Multiple errors", violations);

        // Assert
        assertEquals(3, errorResponse.getViolations().size());
        assertTrue(errorResponse.getViolations().containsKey("field1"));
        assertTrue(errorResponse.getViolations().containsKey("field2"));
        assertTrue(errorResponse.getViolations().containsKey("field3"));
    }
}
