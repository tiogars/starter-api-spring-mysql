package fr.tiogars.starter.route.forms;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RouteUpdateFormTest {

    @Test
    void testDefaultConstructor() {
        // Act
        RouteUpdateForm form = new RouteUpdateForm();

        // Assert
        assertNotNull(form);
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        Long id = 1L;
        String name = "Home";
        String path = "/home";

        // Act
        RouteUpdateForm form = new RouteUpdateForm(id, name, path);

        // Assert
        assertEquals(id, form.getId());
        assertEquals(name, form.getName());
        assertEquals(path, form.getPath());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        RouteUpdateForm form = new RouteUpdateForm();
        Long id = 2L;
        String name = "Dashboard";
        String path = "/dashboard";

        // Act
        form.setId(id);
        form.setName(name);
        form.setPath(path);

        // Assert
        assertEquals(id, form.getId());
        assertEquals(name, form.getName());
        assertEquals(path, form.getPath());
    }

    @Test
    void testWithNullValues() {
        // Arrange
        RouteUpdateForm form = new RouteUpdateForm();

        // Act
        form.setId(null);
        form.setName(null);
        form.setPath(null);

        // Assert
        assertNull(form.getId());
        assertNull(form.getName());
        assertNull(form.getPath());
    }

    @Test
    void testWithZeroId() {
        // Arrange
        RouteUpdateForm form = new RouteUpdateForm();

        // Act
        form.setId(0L);

        // Assert
        assertEquals(0L, form.getId());
    }
}
