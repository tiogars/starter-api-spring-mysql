package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import fr.tiogars.starter.sample.forms.SampleUpdateForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.repositories.SampleRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
class SampleUpdateServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private SampleUpdateService sampleUpdateService;

    private Sample sample;
    private SampleEntity sampleEntity;
    private SampleUpdateForm sampleUpdateForm;
    private Date testDate;

    @BeforeEach
    void setUp() {
        testDate = new Date();

        // Setup Sample model
        sample = new Sample();
        sample.setId(1L);
        sample.setName("UpdatedSample");
        sample.setDescription("Updated Description");
        sample.setActive(true);
        sample.setCreatedAt(testDate);
        sample.setCreatedBy("testUser");
        sample.setUpdatedAt(testDate);
        sample.setUpdatedBy("system");

        // Setup SampleEntity
        sampleEntity = new SampleEntity();
        sampleEntity.setId(1L);
        sampleEntity.setName("UpdatedSample");
        sampleEntity.setDescription("Updated Description");
        sampleEntity.setActive(true);
        sampleEntity.setCreatedAt(testDate);
        sampleEntity.setCreatedBy("testUser");
        sampleEntity.setUpdatedAt(testDate);
        sampleEntity.setUpdatedBy("system");

        // Setup SampleUpdateForm
        sampleUpdateForm = new SampleUpdateForm();
        sampleUpdateForm.setId(1L);
        sampleUpdateForm.setName("UpdatedSample");
        sampleUpdateForm.setDescription("Updated Description");
        sampleUpdateForm.setActive(true);
    }

    @Test
    void testUpdate_ValidForm_ReturnsUpdatedSample() {
        // Arrange
        when(sampleRepository.findById(1L)).thenReturn(java.util.Optional.of(sampleEntity));
        when(validator.validate(any(Sample.class))).thenReturn(Set.of());
        when(sampleRepository.save(any(SampleEntity.class))).thenReturn(sampleEntity);

        // Act
        Sample result = sampleUpdateService.update(sampleUpdateForm);

        // Assert
        assertNotNull(result);
        assertEquals(sampleUpdateForm.getId(), result.getId());
        assertEquals(sampleUpdateForm.getName(), result.getName());
        assertEquals(sampleUpdateForm.getDescription(), result.getDescription());
        assertEquals(sampleUpdateForm.isActive(), result.isActive());
        verify(sampleRepository, times(1)).findById(1L);
        verify(validator, times(1)).validate(any(Sample.class));
        verify(sampleRepository, times(1)).save(any(SampleEntity.class));
    }

    @Test
    void testUpdate_InvalidSample_ThrowsException() {
        // Arrange
        when(sampleRepository.findById(1L)).thenReturn(java.util.Optional.of(sampleEntity));
        @SuppressWarnings("unchecked")
        ConstraintViolation<Sample> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<Sample>> violations = Set.of(violation);
        when(validator.validate(any(Sample.class))).thenReturn(violations);

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> {
            sampleUpdateService.update(sampleUpdateForm);
        });
        verify(sampleRepository, times(1)).findById(1L);
        verify(validator, times(1)).validate(any(Sample.class));
        verify(sampleRepository, never()).save(any(SampleEntity.class));
    }

    @Test
    void testUpdate_SetsUpdatedByToSystem() {
        // Arrange
        when(sampleRepository.findById(1L)).thenReturn(java.util.Optional.of(sampleEntity));
        when(validator.validate(any(Sample.class))).thenReturn(Set.of());
        when(sampleRepository.save(any(SampleEntity.class))).thenReturn(sampleEntity);

        // Act
        Sample result = sampleUpdateService.update(sampleUpdateForm);

        // Assert
        assertNotNull(result);
        assertEquals("system", result.getUpdatedBy());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void testUpdate_WithNullDescription_HandlesGracefully() {
        // Arrange
        sampleUpdateForm.setDescription(null);
        sampleEntity.setDescription(null);
        when(sampleRepository.findById(1L)).thenReturn(java.util.Optional.of(sampleEntity));
        when(validator.validate(any(Sample.class))).thenReturn(Set.of());
        when(sampleRepository.save(any(SampleEntity.class))).thenReturn(sampleEntity);

        // Act
        Sample result = sampleUpdateService.update(sampleUpdateForm);

        // Assert
        assertNotNull(result);
        assertNull(result.getDescription());
        assertEquals(sampleUpdateForm.getName(), result.getName());
    }

    @Test
    void testUpdate_WithInactiveFlag() {
        // Arrange
        sampleUpdateForm.setActive(false);
        sampleEntity.setActive(false);
        when(sampleRepository.findById(1L)).thenReturn(java.util.Optional.of(sampleEntity));
        when(validator.validate(any(Sample.class))).thenReturn(Set.of());
        when(sampleRepository.save(any(SampleEntity.class))).thenReturn(sampleEntity);

        // Act
        Sample result = sampleUpdateService.update(sampleUpdateForm);

        // Assert
        assertNotNull(result);
        assertFalse(result.isActive());
    }

    @Test
    void testUpdate_PreservesId() {
        // Arrange
        Long expectedId = 42L;
        sampleUpdateForm.setId(expectedId);
        sampleEntity.setId(expectedId);
        when(sampleRepository.findById(expectedId)).thenReturn(java.util.Optional.of(sampleEntity));
        when(validator.validate(any(Sample.class))).thenReturn(Set.of());
        when(sampleRepository.save(any(SampleEntity.class))).thenReturn(sampleEntity);

        // Act
        Sample result = sampleUpdateService.update(sampleUpdateForm);

        // Assert
        assertNotNull(result);
        assertEquals(expectedId, result.getId());
    }

    @Test
    void testUpdate_NotFound_ThrowsException() {
        // Arrange
        when(sampleRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sampleUpdateService.update(sampleUpdateForm);
        });
        assertTrue(exception.getMessage().contains("Sample not found"));
        verify(sampleRepository, times(1)).findById(1L);
        verify(validator, never()).validate(any(Sample.class));
        verify(sampleRepository, never()).save(any(SampleEntity.class));
    }

    @Test
    void testUpdate_PreservesCreationFields() {
        // Arrange
        Date originalCreatedAt = new Date(System.currentTimeMillis() - 100000);
        String originalCreatedBy = "originalUser";
        sampleEntity.setCreatedAt(originalCreatedAt);
        sampleEntity.setCreatedBy(originalCreatedBy);
        
        when(sampleRepository.findById(1L)).thenReturn(java.util.Optional.of(sampleEntity));
        when(validator.validate(any(Sample.class))).thenReturn(Set.of());
        when(sampleRepository.save(any(SampleEntity.class))).thenReturn(sampleEntity);

        // Act
        Sample result = sampleUpdateService.update(sampleUpdateForm);

        // Assert
        assertNotNull(result);
        assertEquals(originalCreatedAt, result.getCreatedAt());
        assertEquals(originalCreatedBy, result.getCreatedBy());
    }
}
