package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.forms.SampleCreateForm;
import fr.tiogars.starter.sample.forms.SampleImportForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.models.SampleImportReport;
import fr.tiogars.starter.sample.repositories.SampleRepository;
import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
class SampleImportServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @Mock
    private SampleCreateService sampleCreateService;

    @InjectMocks
    private SampleImportService sampleImportService;

    private SampleImportForm importForm;
    private SampleCreateForm sampleForm1;
    private SampleCreateForm sampleForm2;
    private Sample createdSample;

    @BeforeEach
    void setUp() {
        // Setup sample forms
        sampleForm1 = new SampleCreateForm();
        sampleForm1.setName("Sample1");
        sampleForm1.setDescription("Description 1");
        sampleForm1.setActive(true);

        sampleForm2 = new SampleCreateForm();
        sampleForm2.setName("Sample2");
        sampleForm2.setDescription("Description 2");
        sampleForm2.setActive(true);

        // Setup import form
        importForm = new SampleImportForm();
        importForm.setSamples(Arrays.asList(sampleForm1, sampleForm2));

        // Setup created sample
        createdSample = new Sample();
        createdSample.setId(1L);
        createdSample.setName("Sample1");
        createdSample.setDescription("Description 1");
        createdSample.setActive(true);
    }

    @Test
    void testImportSamples_AllNew_AllCreated() {
        // Arrange
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.empty());
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.empty());
        
        Sample sample1 = new Sample();
        sample1.setId(1L);
        sample1.setName("Sample1");
        
        Sample sample2 = new Sample();
        sample2.setId(2L);
        sample2.setName("Sample2");
        
        when(sampleCreateService.create(sampleForm1)).thenReturn(sample1);
        when(sampleCreateService.create(sampleForm2)).thenReturn(sample2);

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(2, report.getTotalProvided());
        assertEquals(2, report.getTotalCreated());
        assertEquals(0, report.getTotalDuplicates());
        assertEquals(0, report.getTotalErrors());
        assertEquals("success", report.getAlertLevel());
        assertEquals("Successfully imported 2 of 2 samples", report.getMessage());
        assertEquals(2, report.getItems().size());
        
        assertTrue(report.getItems().get(0).isCreated());
        assertEquals("success", report.getItems().get(0).getAlertLevel());
        assertTrue(report.getItems().get(1).isCreated());
        assertEquals("success", report.getItems().get(1).getAlertLevel());
        
        verify(sampleRepository, times(1)).findByName("Sample1");
        verify(sampleRepository, times(1)).findByName("Sample2");
        verify(sampleCreateService, times(1)).create(sampleForm1);
        verify(sampleCreateService, times(1)).create(sampleForm2);
    }

    @Test
    void testImportSamples_OneDuplicate_OneCreatedOneSkipped() {
        // Arrange
        SampleEntity existingEntity = new SampleEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Sample1");
        
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.of(existingEntity));
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.empty());
        
        Sample sample2 = new Sample();
        sample2.setId(2L);
        sample2.setName("Sample2");
        
        when(sampleCreateService.create(sampleForm2)).thenReturn(sample2);

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(2, report.getTotalProvided());
        assertEquals(1, report.getTotalCreated());
        assertEquals(1, report.getTotalDuplicates());
        assertEquals(0, report.getTotalErrors());
        assertEquals("info", report.getAlertLevel());
        assertTrue(report.getMessage().contains("Imported 1 of 2 samples"));
        assertTrue(report.getMessage().contains("1 duplicate"));
        assertEquals(2, report.getItems().size());
        
        assertFalse(report.getItems().get(0).isCreated());
        assertEquals("info", report.getItems().get(0).getAlertLevel());
        assertTrue(report.getItems().get(0).getMessage().contains("already exists"));
        
        assertTrue(report.getItems().get(1).isCreated());
        assertEquals("success", report.getItems().get(1).getAlertLevel());
        
        verify(sampleRepository, times(1)).findByName("Sample1");
        verify(sampleRepository, times(1)).findByName("Sample2");
        verify(sampleCreateService, never()).create(sampleForm1);
        verify(sampleCreateService, times(1)).create(sampleForm2);
    }

    @Test
    void testImportSamples_AllDuplicates_NoneCreated() {
        // Arrange
        SampleEntity existingEntity1 = new SampleEntity();
        existingEntity1.setId(1L);
        existingEntity1.setName("Sample1");
        
        SampleEntity existingEntity2 = new SampleEntity();
        existingEntity2.setId(2L);
        existingEntity2.setName("Sample2");
        
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.of(existingEntity1));
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.of(existingEntity2));

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(2, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(2, report.getTotalDuplicates());
        assertEquals(0, report.getTotalErrors());
        assertEquals("warning", report.getAlertLevel());
        assertEquals("No samples imported. All 2 samples already exist", report.getMessage());
        assertEquals(2, report.getItems().size());
        
        assertFalse(report.getItems().get(0).isCreated());
        assertFalse(report.getItems().get(1).isCreated());
        
        verify(sampleRepository, times(1)).findByName("Sample1");
        verify(sampleRepository, times(1)).findByName("Sample2");
        verify(sampleCreateService, never()).create(any());
    }

    @Test
    void testImportSamples_EmptyList_WarningReturned() {
        // Arrange
        importForm.setSamples(Collections.emptyList());

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(0, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(0, report.getTotalDuplicates());
        assertEquals(0, report.getTotalErrors());
        assertEquals("warning", report.getAlertLevel());
        assertEquals("No samples provided for import", report.getMessage());
        assertEquals(0, report.getItems().size());
        
        verify(sampleRepository, never()).findByName(any());
        verify(sampleCreateService, never()).create(any());
    }

    @Test
    void testImportSamples_NullList_WarningReturned() {
        // Arrange
        importForm.setSamples(null);

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(0, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(0, report.getTotalDuplicates());
        assertEquals(0, report.getTotalErrors());
        assertEquals("warning", report.getAlertLevel());
        assertEquals("No samples provided for import", report.getMessage());
        assertEquals(0, report.getItems().size());
        
        verify(sampleRepository, never()).findByName(any());
        verify(sampleCreateService, never()).create(any());
    }

    @Test
    void testImportSamples_ValidationFailure_ErrorReported() {
        // Arrange
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.empty());
        when(sampleCreateService.create(sampleForm1))
            .thenThrow(new ConstraintViolationException("Validation failed", null));

        SampleImportForm singleItemForm = new SampleImportForm();
        singleItemForm.setSamples(Collections.singletonList(sampleForm1));

        // Act
        SampleImportReport report = sampleImportService.importSamples(singleItemForm);

        // Assert
        assertNotNull(report);
        assertEquals(1, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(0, report.getTotalDuplicates());
        assertEquals(1, report.getTotalErrors());
        assertEquals("error", report.getAlertLevel());
        assertEquals(1, report.getItems().size());
        
        assertFalse(report.getItems().get(0).isCreated());
        assertEquals("error", report.getItems().get(0).getAlertLevel());
        assertTrue(report.getItems().get(0).getMessage().contains("Validation failed"));
        
        verify(sampleRepository, times(1)).findByName("Sample1");
        verify(sampleCreateService, times(1)).create(sampleForm1);
    }

    @Test
    void testImportSamples_GenericException_ErrorReported() {
        // Arrange
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.empty());
        when(sampleCreateService.create(sampleForm1))
            .thenThrow(new RuntimeException("Database error"));

        SampleImportForm singleItemForm = new SampleImportForm();
        singleItemForm.setSamples(Collections.singletonList(sampleForm1));

        // Act
        SampleImportReport report = sampleImportService.importSamples(singleItemForm);

        // Assert
        assertNotNull(report);
        assertEquals(1, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(1, report.getTotalSkipped());
        assertEquals(1, report.getItems().size());
        
        assertFalse(report.getItems().get(0).isCreated());
        assertEquals("error", report.getItems().get(0).getAlertLevel());
        assertTrue(report.getItems().get(0).getMessage().contains("Failed to create sample"));
        
        verify(sampleRepository, times(1)).findByName("Sample1");
        verify(sampleCreateService, times(1)).create(sampleForm1);
    }

    @Test
    void testImportSamples_MixedResults_CorrectReport() {
        // Arrange - 3 samples: one duplicate, one success, one validation error
        SampleCreateForm sampleForm3 = new SampleCreateForm();
        sampleForm3.setName("Sample3");
        sampleForm3.setDescription("Description 3");
        sampleForm3.setActive(true);
        
        importForm.setSamples(Arrays.asList(sampleForm1, sampleForm2, sampleForm3));
        
        SampleEntity existingEntity = new SampleEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Sample1");
        
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.of(existingEntity));
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.empty());
        when(sampleRepository.findByName("Sample3")).thenReturn(Optional.empty());
        
        Sample sample2 = new Sample();
        sample2.setId(2L);
        sample2.setName("Sample2");
        
        when(sampleCreateService.create(sampleForm2)).thenReturn(sample2);
        when(sampleCreateService.create(sampleForm3))
            .thenThrow(new ConstraintViolationException("Invalid", null));

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(3, report.getTotalProvided());
        assertEquals(1, report.getTotalCreated());
        assertEquals(1, report.getTotalDuplicates());
        assertEquals(1, report.getTotalErrors());
        assertEquals("info", report.getAlertLevel());
        assertTrue(report.getMessage().contains("Imported 1 of 3 samples"));
        assertTrue(report.getMessage().contains("1 duplicate"));
        assertTrue(report.getMessage().contains("1 error"));
        assertEquals(3, report.getItems().size());
        
        // First item - duplicate
        assertFalse(report.getItems().get(0).isCreated());
        assertEquals("info", report.getItems().get(0).getAlertLevel());
        
        // Second item - success
        assertTrue(report.getItems().get(1).isCreated());
        assertEquals("success", report.getItems().get(1).getAlertLevel());
        
        // Third item - validation error
        assertFalse(report.getItems().get(2).isCreated());
        assertEquals("error", report.getItems().get(2).getAlertLevel());
    }

    @Test
    void testImportSamples_MultipleDuplicates_CorrectMessage() {
        // Arrange - multiple duplicates
        SampleCreateForm sampleForm3 = new SampleCreateForm();
        sampleForm3.setName("Sample3");
        
        importForm.setSamples(Arrays.asList(sampleForm1, sampleForm2, sampleForm3));
        
        SampleEntity existingEntity1 = new SampleEntity();
        existingEntity1.setId(1L);
        existingEntity1.setName("Sample1");
        
        SampleEntity existingEntity2 = new SampleEntity();
        existingEntity2.setId(2L);
        existingEntity2.setName("Sample2");
        
        SampleEntity existingEntity3 = new SampleEntity();
        existingEntity3.setId(3L);
        existingEntity3.setName("Sample3");
        
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.of(existingEntity1));
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.of(existingEntity2));
        when(sampleRepository.findByName("Sample3")).thenReturn(Optional.of(existingEntity3));

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(3, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(3, report.getTotalDuplicates());
        assertEquals("warning", report.getAlertLevel());
        assertEquals("No samples imported. All 3 samples already exist", report.getMessage());
    }

    @Test
    void testImportSamples_OnlyErrors_ErrorAlertLevel() {
        // Arrange - all fail with validation errors
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.empty());
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.empty());
        
        when(sampleCreateService.create(sampleForm1))
            .thenThrow(new ConstraintViolationException("Validation failed 1", null));
        when(sampleCreateService.create(sampleForm2))
            .thenThrow(new ConstraintViolationException("Validation failed 2", null));

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(2, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(0, report.getTotalDuplicates());
        assertEquals(2, report.getTotalErrors());
        assertEquals("error", report.getAlertLevel());
        assertTrue(report.getMessage().contains("No samples imported"));
        assertTrue(report.getMessage().contains("2 errors"));
    }

    @Test
    void testImportSamples_OnlySkipped_ErrorAlertLevel() {
        // Arrange - all fail with generic exceptions
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.empty());
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.empty());
        
        when(sampleCreateService.create(sampleForm1))
            .thenThrow(new RuntimeException("Database error 1"));
        when(sampleCreateService.create(sampleForm2))
            .thenThrow(new RuntimeException("Database error 2"));

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(2, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(0, report.getTotalDuplicates());
        assertEquals(2, report.getTotalSkipped());
        assertEquals("error", report.getAlertLevel());
        assertTrue(report.getMessage().contains("No samples imported"));
        assertTrue(report.getMessage().contains("2 skipped"));
    }

    @Test
    void testImportSamples_DuplicatesAndErrors_CorrectMessage() {
        // Arrange - mix of duplicates and errors
        SampleEntity existingEntity = new SampleEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Sample1");
        
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.of(existingEntity));
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.empty());
        
        when(sampleCreateService.create(sampleForm2))
            .thenThrow(new ConstraintViolationException("Validation failed", null));

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(2, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(1, report.getTotalDuplicates());
        assertEquals(1, report.getTotalErrors());
        assertEquals("error", report.getAlertLevel());
        assertTrue(report.getMessage().contains("No samples imported"));
        assertTrue(report.getMessage().contains("1 duplicate"));
        assertTrue(report.getMessage().contains("1 error"));
    }

    @Test
    void testImportSamples_DuplicatesAndSkipped_CorrectMessage() {
        // Arrange - mix of duplicates and skipped
        SampleEntity existingEntity = new SampleEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Sample1");
        
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.of(existingEntity));
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.empty());
        
        when(sampleCreateService.create(sampleForm2))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(2, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(1, report.getTotalDuplicates());
        assertEquals(1, report.getTotalSkipped());
        assertEquals("error", report.getAlertLevel());
        assertTrue(report.getMessage().contains("No samples imported"));
        assertTrue(report.getMessage().contains("1 duplicate"));
        assertTrue(report.getMessage().contains("1 skipped"));
    }

    @Test
    void testImportSamples_ErrorsAndSkipped_CorrectMessage() {
        // Arrange - mix of errors and skipped
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.empty());
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.empty());
        
        when(sampleCreateService.create(sampleForm1))
            .thenThrow(new ConstraintViolationException("Validation failed", null));
        when(sampleCreateService.create(sampleForm2))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(2, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(1, report.getTotalErrors());
        assertEquals(1, report.getTotalSkipped());
        assertEquals("error", report.getAlertLevel());
        assertTrue(report.getMessage().contains("No samples imported"));
        assertTrue(report.getMessage().contains("1 error"));
        assertTrue(report.getMessage().contains("1 skipped"));
    }

    @Test
    void testImportSamples_AllThreeTypes_DuplicatesErrorsAndSkipped() {
        // Arrange - duplicates, errors, and skipped
        SampleCreateForm sampleForm3 = new SampleCreateForm();
        sampleForm3.setName("Sample3");
        
        importForm.setSamples(Arrays.asList(sampleForm1, sampleForm2, sampleForm3));
        
        SampleEntity existingEntity = new SampleEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Sample1");
        
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.of(existingEntity));
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.empty());
        when(sampleRepository.findByName("Sample3")).thenReturn(Optional.empty());
        
        when(sampleCreateService.create(sampleForm2))
            .thenThrow(new ConstraintViolationException("Validation failed", null));
        when(sampleCreateService.create(sampleForm3))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(3, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(1, report.getTotalDuplicates());
        assertEquals(1, report.getTotalErrors());
        assertEquals(1, report.getTotalSkipped());
        assertEquals("error", report.getAlertLevel());
        assertTrue(report.getMessage().contains("No samples imported"));
        assertTrue(report.getMessage().contains("1 duplicate"));
        assertTrue(report.getMessage().contains("1 error"));
        assertTrue(report.getMessage().contains("1 skipped"));
    }

    @Test
    void testImportSamples_PartialSuccess_WithAllFailureTypes() {
        // Arrange - one success, one duplicate, one error, one skipped
        SampleCreateForm sampleForm3 = new SampleCreateForm();
        sampleForm3.setName("Sample3");
        sampleForm3.setDescription("Description 3");
        
        SampleCreateForm sampleForm4 = new SampleCreateForm();
        sampleForm4.setName("Sample4");
        sampleForm4.setDescription("Description 4");
        
        importForm.setSamples(Arrays.asList(sampleForm1, sampleForm2, sampleForm3, sampleForm4));
        
        SampleEntity existingEntity = new SampleEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Sample1");
        
        when(sampleRepository.findByName("Sample1")).thenReturn(Optional.of(existingEntity));
        when(sampleRepository.findByName("Sample2")).thenReturn(Optional.empty());
        when(sampleRepository.findByName("Sample3")).thenReturn(Optional.empty());
        when(sampleRepository.findByName("Sample4")).thenReturn(Optional.empty());
        
        Sample sample2 = new Sample();
        sample2.setId(2L);
        sample2.setName("Sample2");
        
        when(sampleCreateService.create(sampleForm2)).thenReturn(sample2);
        when(sampleCreateService.create(sampleForm3))
            .thenThrow(new ConstraintViolationException("Validation failed", null));
        when(sampleCreateService.create(sampleForm4))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        SampleImportReport report = sampleImportService.importSamples(importForm);

        // Assert
        assertNotNull(report);
        assertEquals(4, report.getTotalProvided());
        assertEquals(1, report.getTotalCreated());
        assertEquals(1, report.getTotalDuplicates());
        assertEquals(1, report.getTotalErrors());
        assertEquals(1, report.getTotalSkipped());
        assertEquals("info", report.getAlertLevel());
        assertTrue(report.getMessage().contains("Imported 1 of 4 samples"));
        assertTrue(report.getMessage().contains("1 duplicate"));
        assertTrue(report.getMessage().contains("1 error"));
        assertTrue(report.getMessage().contains("1 skipped"));
    }
}
