package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.repositories.SampleRepository;

/**
 * Branch coverage tests for SampleCrudService.
 * Focuses on covering the null check branch in toModel() method.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SampleCrudServiceBranchTest {
    @Autowired
    private SampleCrudService sampleCrudService;

    @Autowired
    private SampleRepository sampleRepository;

    private SampleEntity entityWithTags;
    private SampleEntity entityWithoutTags;

    @BeforeEach
    void setUp() {
        sampleRepository.deleteAll();
        
        Date testDate = new Date();
        
        // Entity with tags (will have non-null tags)
        entityWithTags = new SampleEntity();
        entityWithTags.setName("WithTags");
        entityWithTags.setDescription("Has tags");
        entityWithTags.setActive(true);
        entityWithTags.setCreatedAt(testDate);
        entityWithTags.setCreatedBy("admin");
        entityWithTags.setUpdatedAt(testDate);
        entityWithTags.setUpdatedBy("admin");
        // Tags will be empty set, not null
        entityWithTags.setTags(java.util.Collections.emptySet());
        entityWithTags = sampleRepository.save(entityWithTags);
        
        // Entity without tags (tags will be null)
        entityWithoutTags = new SampleEntity();
        entityWithoutTags.setName("NoTags");
        entityWithoutTags.setDescription("No tags");
        entityWithoutTags.setActive(false);
        entityWithoutTags.setCreatedAt(testDate);
        entityWithoutTags.setCreatedBy("user");
        entityWithoutTags.setUpdatedAt(testDate);
        entityWithoutTags.setUpdatedBy("user");
        // Tags will be null
        entityWithoutTags.setTags(null);
        entityWithoutTags = sampleRepository.save(entityWithoutTags);
    }

    @Test
    void testFindByIdWithNullTags_CoversBranch() {
        Optional<Sample> result = sampleCrudService.findById(entityWithoutTags.getId());
        
        assertTrue(result.isPresent());
        Sample sample = result.get();
        assertNotNull(sample);
        assertEquals("NoTags", sample.getName());
        // Tags should be null or empty, not cause an error
    }

    @Test
    void testFindByIdWithEmptyTags_CoversBranch() {
        Optional<Sample> result = sampleCrudService.findById(entityWithTags.getId());
        
        assertTrue(result.isPresent());
        Sample sample = result.get();
        assertNotNull(sample);
        assertEquals("WithTags", sample.getName());
        // Should have empty tags set
        assertNotNull(sample.getTags());
    }

    @Test
    void testFindAllWithNullTags() {
        var samples = sampleCrudService.findAll();
        assertEquals(2, samples.size());
    }

    @Test
    void testSaveEntity() {
        SampleEntity entity = new SampleEntity();
        entity.setName("NewSample");
        entity.setDescription("New");
        entity.setActive(true);
        entity.setCreatedAt(new Date());
        entity.setCreatedBy("admin");
        entity.setUpdatedAt(new Date());
        entity.setUpdatedBy("admin");
        entity.setTags(null);
        
        Sample result = sampleCrudService.save(entity);
        assertNotNull(result);
        assertEquals("NewSample", result.getName());
    }

    @Test
    void testDeleteById() {
        long idToDelete = entityWithoutTags.getId();
        sampleCrudService.deleteById(idToDelete);
        
        Optional<Sample> result = sampleCrudService.findById(idToDelete);
        assertTrue(result.isEmpty());
    }
}
