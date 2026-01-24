package fr.tiogars.starter.sample.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.tiogars.starter.tag.entities.TagEntity;

class SampleEntityTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleEntity entity = new SampleEntity();

        // Assert
        assertNotNull(entity);
        assertNotNull(entity.getTags());
        assertTrue(entity.getTags().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleEntity entity = new SampleEntity();
        Long id = 1L;
        String name = "TestSample";
        String description = "Test Description";
        boolean active = true;
        Date createdAt = new Date();
        String createdBy = "testUser";
        Date updatedAt = new Date();
        String updatedBy = "testUser";

        // Act
        entity.setId(id);
        entity.setName(name);
        entity.setDescription(description);
        entity.setActive(active);
        entity.setCreatedAt(createdAt);
        entity.setCreatedBy(createdBy);
        entity.setUpdatedAt(updatedAt);
        entity.setUpdatedBy(updatedBy);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(description, entity.getDescription());
        assertTrue(entity.isActive());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(createdBy, entity.getCreatedBy());
        assertEquals(updatedAt, entity.getUpdatedAt());
        assertEquals(updatedBy, entity.getUpdatedBy());
    }

    @Test
    void testAddTag() {
        // Arrange
        SampleEntity entity = new SampleEntity();
        TagEntity tag = new TagEntity();
        tag.setId(1L);
        tag.setName("TestTag");

        // Act
        entity.addTag(tag);

        // Assert
        assertEquals(1, entity.getTags().size());
        assertTrue(entity.getTags().contains(tag));
    }

    @Test
    void testRemoveTag() {
        // Arrange
        SampleEntity entity = new SampleEntity();
        TagEntity tag = new TagEntity();
        tag.setId(1L);
        tag.setName("TestTag");
        entity.addTag(tag);

        // Act
        entity.removeTag(tag);

        // Assert
        assertTrue(entity.getTags().isEmpty());
    }

    @Test
    void testWithMultipleTags() {
        // Arrange
        SampleEntity entity = new SampleEntity();
        TagEntity tag1 = new TagEntity();
        tag1.setId(1L);
        tag1.setName("Tag1");
        
        TagEntity tag2 = new TagEntity();
        tag2.setId(2L);
        tag2.setName("Tag2");

        // Act
        entity.addTag(tag1);
        entity.addTag(tag2);

        // Assert
        assertEquals(2, entity.getTags().size());
        assertTrue(entity.getTags().contains(tag1));
        assertTrue(entity.getTags().contains(tag2));
    }

    @Test
    void testSetTags() {
        // Arrange
        SampleEntity entity = new SampleEntity();
        Set<TagEntity> tags = new HashSet<>();
        TagEntity tag1 = new TagEntity();
        tag1.setId(1L);
        tag1.setName("Tag1");
        tags.add(tag1);

        // Act
        entity.setTags(tags);

        // Assert
        assertEquals(1, entity.getTags().size());
        assertTrue(entity.getTags().contains(tag1));
    }

    @Test
    void testIsActiveDefaultValue() {
        // Arrange & Act
        SampleEntity entity = new SampleEntity();

        // Assert
        assertFalse(entity.isActive());
    }

    @Test
    void testWithNullDescription() {
        // Arrange
        SampleEntity entity = new SampleEntity();

        // Act
        entity.setDescription(null);

        // Assert
        assertNull(entity.getDescription());
    }

    @Test
    void testAuditFields() {
        // Arrange
        SampleEntity entity = new SampleEntity();
        Date now = new Date();
        String user = "admin";

        // Act
        entity.setCreatedAt(now);
        entity.setCreatedBy(user);
        entity.setUpdatedAt(now);
        entity.setUpdatedBy(user);

        // Assert
        assertEquals(now, entity.getCreatedAt());
        assertEquals(user, entity.getCreatedBy());
        assertEquals(now, entity.getUpdatedAt());
        assertEquals(user, entity.getUpdatedBy());
    }
}
