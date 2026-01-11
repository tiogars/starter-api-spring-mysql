package fr.tiogars.starter.sample.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SampleTagEntityTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleTagEntity entity = new SampleTagEntity();

        // Assert
        assertNotNull(entity);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleTagEntity entity = new SampleTagEntity();
        Long id = 1L;
        String name = "TestTag";
        String description = "Test Description";

        // Act
        entity.setId(id);
        entity.setName(name);
        entity.setDescription(description);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(description, entity.getDescription());
    }

    @Test
    void testWithNullDescription() {
        // Arrange
        SampleTagEntity entity = new SampleTagEntity();

        // Act
        entity.setName("Tag");
        entity.setDescription(null);

        // Assert
        assertEquals("Tag", entity.getName());
        assertNull(entity.getDescription());
    }

    @Test
    void testWithLongName() {
        // Arrange
        SampleTagEntity entity = new SampleTagEntity();
        String longName = "A".repeat(100);

        // Act
        entity.setName(longName);

        // Assert
        assertEquals(longName, entity.getName());
        assertEquals(100, entity.getName().length());
    }

    @Test
    void testWithLongDescription() {
        // Arrange
        SampleTagEntity entity = new SampleTagEntity();
        String longDescription = "A".repeat(255);

        // Act
        entity.setDescription(longDescription);

        // Assert
        assertEquals(longDescription, entity.getDescription());
        assertEquals(255, entity.getDescription().length());
    }
}
