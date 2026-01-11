package fr.tiogars.starter.sample.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SampleTagTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleTag tag = new SampleTag();

        // Assert
        assertNotNull(tag);
    }

    @Test
    void testTwoArgConstructor() {
        // Arrange
        Long id = 1L;
        String name = "TestTag";

        // Act
        SampleTag tag = new SampleTag(id, name);

        // Assert
        assertEquals(id, tag.getId());
        assertEquals(name, tag.getName());
        assertNull(tag.getDescription());
    }

    @Test
    void testThreeArgConstructor() {
        // Arrange
        Long id = 1L;
        String name = "TestTag";
        String description = "Test Description";

        // Act
        SampleTag tag = new SampleTag(id, name, description);

        // Assert
        assertEquals(id, tag.getId());
        assertEquals(name, tag.getName());
        assertEquals(description, tag.getDescription());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleTag tag = new SampleTag();
        Long id = 2L;
        String name = "NewTag";
        String description = "New Description";

        // Act
        tag.setId(id);
        tag.setName(name);
        tag.setDescription(description);

        // Assert
        assertEquals(id, tag.getId());
        assertEquals(name, tag.getName());
        assertEquals(description, tag.getDescription());
    }

    @Test
    void testWithNullDescription() {
        // Arrange & Act
        SampleTag tag = new SampleTag(1L, "Tag", null);

        // Assert
        assertEquals(1L, tag.getId());
        assertEquals("Tag", tag.getName());
        assertNull(tag.getDescription());
    }

    @Test
    void testSetNullValues() {
        // Arrange
        SampleTag tag = new SampleTag(1L, "Tag", "Description");

        // Act
        tag.setId(null);
        tag.setName(null);
        tag.setDescription(null);

        // Assert
        assertNull(tag.getId());
        assertNull(tag.getName());
        assertNull(tag.getDescription());
    }

    @Test
    void testWithLongName() {
        // Arrange
        String longName = "A".repeat(100);
        SampleTag tag = new SampleTag();

        // Act
        tag.setName(longName);

        // Assert
        assertEquals(longName, tag.getName());
        assertEquals(100, tag.getName().length());
    }

    @Test
    void testWithLongDescription() {
        // Arrange
        String longDescription = "A".repeat(255);
        SampleTag tag = new SampleTag();

        // Act
        tag.setDescription(longDescription);

        // Assert
        assertEquals(longDescription, tag.getDescription());
        assertEquals(255, tag.getDescription().length());
    }
}
