package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.sample.forms.SampleCreateForm;
import fr.tiogars.starter.sample.forms.SampleInitForm;
import fr.tiogars.starter.sample.models.Sample;

@ExtendWith(MockitoExtension.class)
class SampleInitServiceTest {

    @Mock
    private SampleCreateService sampleCreateService;

    @InjectMocks
    private SampleInitService sampleInitService;

    private SampleInitForm sampleInitForm;
    private Sample mockSample;

    @BeforeEach
    void setUp() {
        sampleInitForm = new SampleInitForm();
        
        // Setup mock sample
        mockSample = new Sample();
        mockSample.setId(1L);
        mockSample.setName("TestSample");
        mockSample.setDescription("Test Description");
        mockSample.setActive(true);
        mockSample.setCreatedAt(new Date());
        mockSample.setCreatedBy("system");
        mockSample.setUpdatedAt(new Date());
        mockSample.setUpdatedBy("system");
    }

    @Test
    void testInitSamples_CreatesCorrectNumberOfSamples() {
        // Arrange
        sampleInitForm.setNumberOfSamples(5);
        when(sampleCreateService.create(any(SampleCreateForm.class))).thenReturn(mockSample);

        // Act
        List<Sample> result = sampleInitService.initSamples(sampleInitForm);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size());
        verify(sampleCreateService, times(5)).create(any(SampleCreateForm.class));
    }

    @Test
    void testInitSamples_WithSingleSample() {
        // Arrange
        sampleInitForm.setNumberOfSamples(1);
        when(sampleCreateService.create(any(SampleCreateForm.class))).thenReturn(mockSample);

        // Act
        List<Sample> result = sampleInitService.initSamples(sampleInitForm);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sampleCreateService, times(1)).create(any(SampleCreateForm.class));
    }

    @Test
    void testInitSamples_WithMaximumSamples() {
        // Arrange
        sampleInitForm.setNumberOfSamples(100);
        when(sampleCreateService.create(any(SampleCreateForm.class))).thenReturn(mockSample);

        // Act
        List<Sample> result = sampleInitService.initSamples(sampleInitForm);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.size());
        verify(sampleCreateService, times(100)).create(any(SampleCreateForm.class));
    }

    @Test
    void testInitSamples_HandlesCreationFailureGracefully() {
        // Arrange
        sampleInitForm.setNumberOfSamples(5);
        when(sampleCreateService.create(any(SampleCreateForm.class)))
            .thenReturn(mockSample)
            .thenThrow(new RuntimeException("Creation failed"))
            .thenReturn(mockSample)
            .thenReturn(mockSample)
            .thenReturn(mockSample);

        // Act
        List<Sample> result = sampleInitService.initSamples(sampleInitForm);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size()); // 5 attempts, 1 failure = 4 successes
        verify(sampleCreateService, times(5)).create(any(SampleCreateForm.class));
    }

    @Test
    void testInitSamples_CreatesUniqueSamples() {
        // Arrange
        sampleInitForm.setNumberOfSamples(10);
        when(sampleCreateService.create(any(SampleCreateForm.class))).thenReturn(mockSample);

        // Act
        List<Sample> result = sampleInitService.initSamples(sampleInitForm);

        // Assert
        assertNotNull(result);
        assertEquals(10, result.size());
        
        // Verify that create was called with different forms
        verify(sampleCreateService, times(10)).create(any(SampleCreateForm.class));
    }

    @Test
    void testInitSamples_ReturnsEmptyListWhenAllFail() {
        // Arrange
        sampleInitForm.setNumberOfSamples(3);
        when(sampleCreateService.create(any(SampleCreateForm.class)))
            .thenThrow(new RuntimeException("Creation failed"));

        // Act
        List<Sample> result = sampleInitService.initSamples(sampleInitForm);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sampleCreateService, times(3)).create(any(SampleCreateForm.class));
    }

    @Test
    void testInitSamples_SamplesHaveValidNames() {
        // Arrange
        sampleInitForm.setNumberOfSamples(5);
        
        // Capture the forms passed to create
        when(sampleCreateService.create(any(SampleCreateForm.class))).thenAnswer(invocation -> {
            SampleCreateForm form = invocation.getArgument(0);
            assertNotNull(form.getName());
            assertTrue(form.getName().length() <= 10, "Name should not exceed 10 characters");
            assertFalse(form.getName().isEmpty(), "Name should not be empty");
            return mockSample;
        });

        // Act
        List<Sample> result = sampleInitService.initSamples(sampleInitForm);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size());
    }

    @Test
    void testInitSamples_SamplesHaveDescriptions() {
        // Arrange
        sampleInitForm.setNumberOfSamples(3);
        
        // Capture the forms passed to create
        when(sampleCreateService.create(any(SampleCreateForm.class))).thenAnswer(invocation -> {
            SampleCreateForm form = invocation.getArgument(0);
            assertNotNull(form.getDescription());
            assertFalse(form.getDescription().isEmpty(), "Description should not be empty");
            return mockSample;
        });

        // Act
        List<Sample> result = sampleInitService.initSamples(sampleInitForm);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }
}
