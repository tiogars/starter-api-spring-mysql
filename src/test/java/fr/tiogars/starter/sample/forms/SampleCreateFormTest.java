package fr.tiogars.starter.sample.forms;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class SampleCreateFormTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleCreateForm form = new SampleCreateForm();

        // Assert
        assertNotNull(form);
        assertNotNull(form.getTagNames());
        assertTrue(form.getTagNames().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleCreateForm form = new SampleCreateForm();
        String name = "TestSample";
        String description = "Test Description";
        boolean active = true;
        Set<String> tagNames = new HashSet<>();
        tagNames.add("Tag1");
        tagNames.add("Tag2");

        // Act
        form.setName(name);
        form.setDescription(description);
        form.setActive(active);
        form.setTagNames(tagNames);

        // Assert
        assertEquals(name, form.getName());
        assertEquals(description, form.getDescription());
        assertTrue(form.isActive());
        assertEquals(tagNames, form.getTagNames());
        assertEquals(2, form.getTagNames().size());
    }

    @Test
    void testIsActiveDefaultValue() {
        // Arrange & Act
        SampleCreateForm form = new SampleCreateForm();

        // Assert
        assertFalse(form.isActive());
    }

    @Test
    void testSetActiveToTrue() {
        // Arrange
        SampleCreateForm form = new SampleCreateForm();

        // Act
        form.setActive(true);

        // Assert
        assertTrue(form.isActive());
    }

    @Test
    void testWithNullDescription() {
        // Arrange
        SampleCreateForm form = new SampleCreateForm();

        // Act
        form.setDescription(null);

        // Assert
        assertNull(form.getDescription());
    }

    @Test
    void testWithEmptyTagNames() {
        // Arrange
        SampleCreateForm form = new SampleCreateForm();
        Set<String> emptyTags = new HashSet<>();

        // Act
        form.setTagNames(emptyTags);

        // Assert
        assertNotNull(form.getTagNames());
        assertTrue(form.getTagNames().isEmpty());
    }

    @Test
    void testWithMultipleTagNames() {
        // Arrange
        SampleCreateForm form = new SampleCreateForm();
        Set<String> tags = new HashSet<>();
        tags.add("Important");
        tags.add("Urgent");
        tags.add("Review");

        // Act
        form.setTagNames(tags);

        // Assert
        assertEquals(3, form.getTagNames().size());
        assertTrue(form.getTagNames().contains("Important"));
        assertTrue(form.getTagNames().contains("Urgent"));
        assertTrue(form.getTagNames().contains("Review"));
    }

    @Test
    void testWithNullName() {
        // Arrange
        SampleCreateForm form = new SampleCreateForm();

        // Act
        form.setName(null);

        // Assert
        assertNull(form.getName());
    }
}
