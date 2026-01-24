package fr.tiogars.starter.sample.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.tiogars.starter.tag.models.Tag;

class SampleTest {

    @Test
    void testDefaultConstructor() {
        // Act
        Sample sample = new Sample();

        // Assert
        assertNotNull(sample);
        assertNotNull(sample.getTags());
        assertTrue(sample.getTags().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Sample sample = new Sample();
        Long id = 1L;
        String name = "TestSample";
        String description = "Test Description";
        boolean active = true;
        Date createdAt = new Date();
        String createdBy = "testUser";
        Date updatedAt = new Date();
        String updatedBy = "testUser";

        // Act
        sample.setId(id);
        sample.setName(name);
        sample.setDescription(description);
        sample.setActive(active);
        sample.setCreatedAt(createdAt);
        sample.setCreatedBy(createdBy);
        sample.setUpdatedAt(updatedAt);
        sample.setUpdatedBy(updatedBy);

        // Assert
        assertEquals(id, sample.getId());
        assertEquals(name, sample.getName());
        assertEquals(description, sample.getDescription());
        assertTrue(sample.isActive());
        assertEquals(createdAt, sample.getCreatedAt());
        assertEquals(createdBy, sample.getCreatedBy());
        assertEquals(updatedAt, sample.getUpdatedAt());
        assertEquals(updatedBy, sample.getUpdatedBy());
    }

    @Test
    void testWithTags() {
        // Arrange
        Sample sample = new Sample();
        Set<Tag> tags = new HashSet<>();
        Tag tag1 = new Tag(1L, "Tag1");
        Tag tag2 = new Tag(2L, "Tag2");
        tags.add(tag1);
        tags.add(tag2);

        // Act
        sample.setTags(tags);

        // Assert
        assertEquals(2, sample.getTags().size());
        assertTrue(sample.getTags().contains(tag1));
        assertTrue(sample.getTags().contains(tag2));
    }

    @Test
    void testIsActiveDefaultValue() {
        // Arrange & Act
        Sample sample = new Sample();

        // Assert
        assertFalse(sample.isActive());
    }

    @Test
    void testSetActiveToTrue() {
        // Arrange
        Sample sample = new Sample();

        // Act
        sample.setActive(true);

        // Assert
        assertTrue(sample.isActive());
    }

    @Test
    void testWithNullDescription() {
        // Arrange
        Sample sample = new Sample();

        // Act
        sample.setDescription(null);

        // Assert
        assertNull(sample.getDescription());
    }

    @Test
    void testWithEmptyTags() {
        // Arrange
        Sample sample = new Sample();
        Set<Tag> emptyTags = new HashSet<>();

        // Act
        sample.setTags(emptyTags);

        // Assert
        assertNotNull(sample.getTags());
        assertTrue(sample.getTags().isEmpty());
    }

    @Test
    void testWithMaxLengthName() {
        // Arrange
        Sample sample = new Sample();
        String maxName = "A".repeat(10);

        // Act
        sample.setName(maxName);

        // Assert
        assertEquals(maxName, sample.getName());
        assertEquals(10, sample.getName().length());
    }

    @Test
    void testAuditFields() {
        // Arrange
        Sample sample = new Sample();
        Date now = new Date();
        String user = "admin";

        // Act
        sample.setCreatedAt(now);
        sample.setCreatedBy(user);
        sample.setUpdatedAt(now);
        sample.setUpdatedBy(user);

        // Assert
        assertEquals(now, sample.getCreatedAt());
        assertEquals(user, sample.getCreatedBy());
        assertEquals(now, sample.getUpdatedAt());
        assertEquals(user, sample.getUpdatedBy());
    }
}
