package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.sample.entities.SampleTagEntity;
import fr.tiogars.starter.sample.models.SampleTag;
import fr.tiogars.starter.sample.repositories.SampleTagRepository;

@ExtendWith(MockitoExtension.class)
class SampleTagServiceTest {

    @Mock
    private SampleTagRepository sampleTagRepository;

    @InjectMocks
    private SampleTagService sampleTagService;

    private SampleTagEntity tagEntity;

    @BeforeEach
    void setUp() {
        tagEntity = new SampleTagEntity();
        tagEntity.setId(1L);
        tagEntity.setName("TestTag");
        tagEntity.setDescription("Test Description");
    }

    @Test
    void testFindAll_ReturnsListOfTags() {
        // Arrange
        SampleTagEntity entity2 = new SampleTagEntity();
        entity2.setId(2L);
        entity2.setName("Tag2");
        entity2.setDescription("Description 2");

        when(sampleTagRepository.findAll()).thenReturn(Arrays.asList(tagEntity, entity2));

        // Act
        List<SampleTag> result = sampleTagService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("TestTag", result.get(0).getName());
        assertEquals("Tag2", result.get(1).getName());
        verify(sampleTagRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsEmptyList() {
        // Arrange
        when(sampleTagRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<SampleTag> result = sampleTagService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(sampleTagRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ReturnsTagWhenExists() {
        // Arrange
        when(sampleTagRepository.findById(1L)).thenReturn(Optional.of(tagEntity));

        // Act
        Optional<SampleTag> result = sampleTagService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("TestTag", result.get().getName());
        assertEquals("Test Description", result.get().getDescription());
        verify(sampleTagRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_ReturnsEmptyWhenNotExists() {
        // Arrange
        when(sampleTagRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<SampleTag> result = sampleTagService.findById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(sampleTagRepository, times(1)).findById(999L);
    }

    @Test
    void testFindByName_ReturnsTagWhenExists() {
        // Arrange
        when(sampleTagRepository.findByName("TestTag")).thenReturn(Optional.of(tagEntity));

        // Act
        Optional<SampleTag> result = sampleTagService.findByName("TestTag");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("TestTag", result.get().getName());
        verify(sampleTagRepository, times(1)).findByName("TestTag");
    }

    @Test
    void testFindByName_ReturnsEmptyWhenNotExists() {
        // Arrange
        when(sampleTagRepository.findByName("NonExistent")).thenReturn(Optional.empty());

        // Act
        Optional<SampleTag> result = sampleTagService.findByName("NonExistent");

        // Assert
        assertFalse(result.isPresent());
        verify(sampleTagRepository, times(1)).findByName("NonExistent");
    }

    @Test
    void testFindOrCreateTags_WithNullList_ReturnsEmptyList() {
        // Act
        List<SampleTagEntity> result = sampleTagService.findOrCreateTags(null);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(sampleTagRepository, never()).findByNameIn(any());
    }

    @Test
    void testFindOrCreateTags_WithEmptyList_ReturnsEmptyList() {
        // Act
        List<SampleTagEntity> result = sampleTagService.findOrCreateTags(Arrays.asList());

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(sampleTagRepository, never()).findByNameIn(any());
    }

    @Test
    void testFindOrCreateTags_WithExistingTags_ReturnsExistingTags() {
        // Arrange
        List<String> tagNames = Arrays.asList("TestTag", "Tag2");
        SampleTagEntity tag2 = new SampleTagEntity();
        tag2.setId(2L);
        tag2.setName("Tag2");

        when(sampleTagRepository.findByNameIn(tagNames)).thenReturn(Arrays.asList(tagEntity, tag2));

        // Act
        List<SampleTagEntity> result = sampleTagService.findOrCreateTags(tagNames);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sampleTagRepository, times(1)).findByNameIn(tagNames);
        verify(sampleTagRepository, never()).save(any());
    }

    @Test
    void testFindOrCreateTags_WithNewTags_CreatesAndReturnsAllTags() {
        // Arrange
        List<String> tagNames = Arrays.asList("TestTag", "NewTag");
        SampleTagEntity newTag = new SampleTagEntity();
        newTag.setId(3L);
        newTag.setName("NewTag");

        when(sampleTagRepository.findByNameIn(tagNames)).thenReturn(Arrays.asList(tagEntity));
        when(sampleTagRepository.save(any(SampleTagEntity.class))).thenReturn(newTag);

        // Act
        List<SampleTagEntity> result = sampleTagService.findOrCreateTags(tagNames);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sampleTagRepository, times(1)).findByNameIn(tagNames);
        verify(sampleTagRepository, times(1)).save(any(SampleTagEntity.class));
    }

    @Test
    void testCreate_ReturnsSavedTag() {
        // Arrange
        SampleTag tag = new SampleTag(null, "NewTag", "New Description");
        when(sampleTagRepository.save(any(SampleTagEntity.class))).thenReturn(tagEntity);

        // Act
        SampleTag result = sampleTagService.create(tag);

        // Assert
        assertNotNull(result);
        assertEquals("TestTag", result.getName());
        assertEquals("Test Description", result.getDescription());
        verify(sampleTagRepository, times(1)).save(any(SampleTagEntity.class));
    }

    @Test
    void testDeleteById_CallsRepository() {
        // Arrange
        Long idToDelete = 1L;
        doNothing().when(sampleTagRepository).deleteById(idToDelete);

        // Act
        sampleTagService.deleteById(idToDelete);

        // Assert
        verify(sampleTagRepository, times(1)).deleteById(idToDelete);
    }
}
