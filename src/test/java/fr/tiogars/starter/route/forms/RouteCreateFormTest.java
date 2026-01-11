package fr.tiogars.starter.route.forms;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RouteCreateFormTest {

    @Test
    void testDefaultConstructor() {
        // Act
        RouteCreateForm form = new RouteCreateForm();

        // Assert
        assertNotNull(form);
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String name = "Home";
        String path = "/home";

        // Act
        RouteCreateForm form = new RouteCreateForm(name, path);

        // Assert
        assertEquals(name, form.getName());
        assertEquals(path, form.getPath());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        RouteCreateForm form = new RouteCreateForm();
        String name = "Dashboard";
        String path = "/dashboard";

        // Act
        form.setName(name);
        form.setPath(path);

        // Assert
        assertEquals(name, form.getName());
        assertEquals(path, form.getPath());
    }

    @Test
    void testWithNullValues() {
        // Arrange
        RouteCreateForm form = new RouteCreateForm();

        // Act
        form.setName(null);
        form.setPath(null);

        // Assert
        assertNull(form.getName());
        assertNull(form.getPath());
    }

    @Test
    void testWithRootPath() {
        // Arrange
        RouteCreateForm form = new RouteCreateForm();

        // Act
        form.setName("Root");
        form.setPath("/");

        // Assert
        assertEquals("Root", form.getName());
        assertEquals("/", form.getPath());
    }
}
