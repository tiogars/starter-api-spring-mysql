package fr.tiogars.starter.common.exception;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private ConstraintViolation<?> violation1;

    @Mock
    private ConstraintViolation<?> violation2;

    @Mock
    private Path propertyPath1;

    @Mock
    private Path propertyPath2;

    @Test
    void testHandleConstraintViolationException_ReturnsErrorResponse() {
        // Arrange
        when(propertyPath1.toString()).thenReturn("field1");
        when(propertyPath2.toString()).thenReturn("field2");
        when(violation1.getPropertyPath()).thenReturn(propertyPath1);
        when(violation1.getMessage()).thenReturn("Field1 is invalid");
        when(violation2.getPropertyPath()).thenReturn(propertyPath2);
        when(violation2.getMessage()).thenReturn("Field2 is required");

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation1);
        violations.add(violation2);

        ConstraintViolationException exception = new ConstraintViolationException(violations);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Validation Failed", errorResponse.getError());
        assertEquals("Les données fournies ne sont pas valides", errorResponse.getMessage());
        
        assertNotNull(errorResponse.getViolations());
        assertEquals(2, errorResponse.getViolations().size());
        assertTrue(errorResponse.getViolations().containsKey("field1"));
        assertTrue(errorResponse.getViolations().containsKey("field2"));
        assertEquals("Field1 is invalid", errorResponse.getViolations().get("field1"));
        assertEquals("Field2 is required", errorResponse.getViolations().get("field2"));
    }

    @Test
    void testHandleConstraintViolationException_WithSingleViolation() {
        // Arrange
        when(propertyPath1.toString()).thenReturn("field1");
        when(violation1.getPropertyPath()).thenReturn(propertyPath1);
        when(violation1.getMessage()).thenReturn("Field1 is invalid");

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation1);

        ConstraintViolationException exception = new ConstraintViolationException(violations);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.getViolations().size());
        assertTrue(errorResponse.getViolations().containsKey("field1"));
    }

    @Test
    void testHandleConstraintViolationException_WithEmptyViolations() {
        // Arrange
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolationException exception = new ConstraintViolationException(violations);

        // Act
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleConstraintViolationException(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(0, errorResponse.getViolations().size());
    }
}
