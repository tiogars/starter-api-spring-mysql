package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.forms.SampleCreateForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.repositories.SampleRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
class SampleCreateServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private SampleCreateService sampleCreateService;

    private Sample sample;
    private SampleEntity sampleEntity;
    private SampleCreateForm sampleCreateForm;
    private Date testDate;

    @BeforeEach
    void setUp() {
        testDate = new Date();

        // Setup Sample model
        sample = new Sample();
        sample.setName("Test Sample");
        sample.setDescription("Test Description");
        sample.setActive(true);
        sample.setCreatedAt(testDate);
        sample.setCreatedBy("testUser");
        sample.setUpdatedAt(testDate);
        sample.setUpdatedBy("testUser");

        // Setup SampleEntity
        sampleEntity = new SampleEntity();
        sampleEntity.setId(1L);
        sampleEntity.setName("Test Sample");
        sampleEntity.setDescription("Test Description");
        sampleEntity.setActive(true);
        sampleEntity.setCreatedAt(testDate);
        sampleEntity.setCreatedBy("testUser");
        sampleEntity.setUpdatedAt(testDate);
        sampleEntity.setUpdatedBy("testUser");

        // Setup SampleCreateForm
        sampleCreateForm = new SampleCreateForm();
        sampleCreateForm.setName("Test Sample");
        sampleCreateForm.setDescription("Test Description");
        sampleCreateForm.setActive(true);
    }

    @Test
    void testToEntity_ConvertsModelToEntity() {
        // Act
        SampleEntity result = sampleCreateService.toEntity(sample);

        // Assert
        assertNotNull(result);
        assertEquals(sample.getName(), result.getName());
        assertEquals(sample.getDescription(), result.getDescription());
        assertEquals(sample.isActive(), result.isActive());
        assertEquals(sample.getCreatedAt(), result.getCreatedAt());
        assertEquals(sample.getCreatedBy(), result.getCreatedBy());
        assertEquals(sample.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(sample.getUpdatedBy(), result.getUpdatedBy());
    }

    @Test
    void testToModelFromEntity_ConvertsEntityToModel() {
        // Act
        Sample result = sampleCreateService.toModel(sampleEntity);

        // Assert
        assertNotNull(result);
        assertEquals(sampleEntity.getId(), result.getId());
        assertEquals(sampleEntity.getName(), result.getName());
        assertEquals(sampleEntity.getDescription(), result.getDescription());
        assertEquals(sampleEntity.isActive(), result.isActive());
        assertEquals(sampleEntity.getCreatedAt(), result.getCreatedAt());
        assertEquals(sampleEntity.getCreatedBy(), result.getCreatedBy());
        assertEquals(sampleEntity.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(sampleEntity.getUpdatedBy(), result.getUpdatedBy());
    }

    @Test
    void testToModelFromForm_ConvertsFormToModel() {
        // Act
        Sample result = sampleCreateService.toModel(sampleCreateForm);

        // Assert
        assertNotNull(result);
        assertEquals(sampleCreateForm.getName(), result.getName());
        assertEquals(sampleCreateForm.getDescription(), result.getDescription());
        assertEquals(sampleCreateForm.isActive(), result.isActive());
        assertEquals("system", result.getCreatedBy());
        assertEquals("system", result.getUpdatedBy());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void testToModelFromForm_SetsSystemDefaults() {
        // Act
        Sample result = sampleCreateService.toModel(sampleCreateForm);

        // Assert
        assertNotNull(result);
        assertEquals("system", result.getCreatedBy());
        assertEquals("system", result.getUpdatedBy());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void testValidate_ValidSample_NoException() {
        // Arrange
        when(validator.validate(sample)).thenReturn(Set.of());

        // Act & Assert
        assertDoesNotThrow(() -> sampleCreateService.validate(sample));
        verify(validator, times(1)).validate(sample);
    }

    @Test
    void testValidate_InvalidSample_ThrowsException() {
        // Arrange
        @SuppressWarnings("unchecked")
        ConstraintViolation<Sample> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<Sample>> violations = Set.of(violation);
        when(validator.validate(sample)).thenReturn(violations);

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> {
            sampleCreateService.validate(sample);
        });
        verify(validator, times(1)).validate(sample);
    }

    @Test
    void testToEntity_WithNullDescription_HandlesGracefully() {
        // Arrange
        sample.setDescription(null);

        // Act
        SampleEntity result = sampleCreateService.toEntity(sample);

        // Assert
        assertNotNull(result);
        assertNull(result.getDescription());
        assertEquals(sample.getName(), result.getName());
    }

    @Test
    void testToModelFromForm_WithInactiveFlag() {
        // Arrange
        sampleCreateForm.setActive(false);

        // Act
        Sample result = sampleCreateService.toModel(sampleCreateForm);

        // Assert
        assertNotNull(result);
        assertFalse(result.isActive());
    }
}
