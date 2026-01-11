package fr.tiogars.starter.sample.forms;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

class SampleUpdateFormTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleUpdateForm form = new SampleUpdateForm();

        // Assert
        assertNotNull(form);
        assertNotNull(form.getTagNames());
        assertTrue(form.getTagNames().isEmpty());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        Long id = 1L;
        String name = "UpdatedSample";
        String description = "Updated Description";
        boolean active = true;

        // Act
        SampleUpdateForm form = new SampleUpdateForm(id, name, description, active);

        // Assert
        assertEquals(id, form.getId());
        assertEquals(name, form.getName());
        assertEquals(description, form.getDescription());
        assertTrue(form.isActive());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleUpdateForm form = new SampleUpdateForm();
        Long id = 2L;
        String name = "TestSample";
        String description = "Test Description";
        boolean active = false;
        Set<String> tagNames = new HashSet<>();
        tagNames.add("Tag1");

        // Act
        form.setId(id);
        form.setName(name);
        form.setDescription(description);
        form.setActive(active);
        form.setTagNames(tagNames);

        // Assert
        assertEquals(id, form.getId());
        assertEquals(name, form.getName());
        assertEquals(description, form.getDescription());
        assertFalse(form.isActive());
        assertEquals(tagNames, form.getTagNames());
    }

    @Test
    void testIsActiveDefaultValue() {
        // Arrange & Act
        SampleUpdateForm form = new SampleUpdateForm();

        // Assert
        assertFalse(form.isActive());
    }

    @Test
    void testWithNullDescription() {
        // Arrange
        SampleUpdateForm form = new SampleUpdateForm();

        // Act
        form.setDescription(null);

        // Assert
        assertNull(form.getDescription());
    }

    @Test
    void testWithEmptyTagNames() {
        // Arrange
        SampleUpdateForm form = new SampleUpdateForm();
        Set<String> emptyTags = new HashSet<>();

        // Act
        form.setTagNames(emptyTags);

        // Assert
        assertNotNull(form.getTagNames());
        assertTrue(form.getTagNames().isEmpty());
    }
}
