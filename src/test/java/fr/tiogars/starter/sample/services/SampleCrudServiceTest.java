package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.repositories.SampleRepository;

@ExtendWith(MockitoExtension.class)
class SampleCrudServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @InjectMocks
    private SampleCrudService sampleCrudService;

    private SampleEntity sampleEntity;
    private Date testDate;

    @BeforeEach
    void setUp() {
        testDate = new Date();
        sampleEntity = new SampleEntity();
        sampleEntity.setId(1L);
        sampleEntity.setName("TestSample");
        sampleEntity.setDescription("Test Description");
        sampleEntity.setActive(true);
        sampleEntity.setCreatedAt(testDate);
        sampleEntity.setCreatedBy("testUser");
        sampleEntity.setUpdatedAt(testDate);
        sampleEntity.setUpdatedBy("testUser");
    }

    @Test
    void testFindAll_ReturnsListOfSamples() {
        // Arrange
        SampleEntity entity2 = new SampleEntity();
        entity2.setId(2L);
        entity2.setName("Sample 2");
        entity2.setDescription("Description 2");
        entity2.setActive(false);
        entity2.setCreatedAt(testDate);
        entity2.setCreatedBy("user2");
        entity2.setUpdatedAt(testDate);
        entity2.setUpdatedBy("user2");

        when(sampleRepository.findAll()).thenReturn(Arrays.asList(sampleEntity, entity2));

        // Act
        List<Sample> result = sampleCrudService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("TestSample", result.get(0).getName());
        assertEquals("Sample 2", result.get(1).getName());
        verify(sampleRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsEmptyList() {
        // Arrange
        when(sampleRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Sample> result = sampleCrudService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(sampleRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ReturnsSampleWhenExists() {
        // Arrange
        when(sampleRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));

        // Act
        Optional<Sample> result = sampleCrudService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("TestSample", result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        assertEquals(true, result.get().isActive());
        verify(sampleRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_ReturnsEmptyWhenNotExists() {
        // Arrange
        when(sampleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Sample> result = sampleCrudService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(sampleRepository, times(1)).findById(999L);
    }

    @Test
    void testSave_ReturnsSavedSample() {
        // Arrange
        when(sampleRepository.save(any(SampleEntity.class))).thenReturn(sampleEntity);

        // Act
        Sample result = sampleCrudService.save(sampleEntity);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TestSample", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertTrue(result.isActive());
        assertEquals(testDate, result.getCreatedAt());
        assertEquals("testUser", result.getCreatedBy());
        verify(sampleRepository, times(1)).save(sampleEntity);
    }

    @Test
    void testDeleteById_CallsRepository() {
        // Arrange
        Long idToDelete = 1L;
        doNothing().when(sampleRepository).deleteById(idToDelete);

        // Act
        sampleCrudService.deleteById(idToDelete);

        // Assert
        verify(sampleRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void testToModel_ConvertsEntityToModel() {
        // Act
        Sample result = sampleCrudService.save(sampleEntity);

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
}
