package fr.tiogars.starter.route.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RouteTest {

    @Test
    void testDefaultConstructor() {
        // Act
        Route route = new Route();

        // Assert
        assertNotNull(route);
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String name = "Home";
        String path = "/home";

        // Act
        Route route = new Route(name, path);

        // Assert
        assertEquals(name, route.getName());
        assertEquals(path, route.getPath());
        assertNull(route.getId());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        Route route = new Route();
        Long id = 1L;
        String name = "Dashboard";
        String path = "/dashboard";

        // Act
        route.setId(id);
        route.setName(name);
        route.setPath(path);

        // Assert
        assertEquals(id, route.getId());
        assertEquals(name, route.getName());
        assertEquals(path, route.getPath());
    }

    @Test
    void testWithNullValues() {
        // Arrange
        Route route = new Route();

        // Act
        route.setId(null);
        route.setName(null);
        route.setPath(null);

        // Assert
        assertNull(route.getId());
        assertNull(route.getName());
        assertNull(route.getPath());
    }

    @Test
    void testWithRootPath() {
        // Arrange
        Route route = new Route();

        // Act
        route.setName("Root");
        route.setPath("/");

        // Assert
        assertEquals("Root", route.getName());
        assertEquals("/", route.getPath());
    }

    @Test
    void testWithNestedPath() {
        // Arrange
        Route route = new Route();

        // Act
        route.setName("Settings");
        route.setPath("/admin/settings");

        // Assert
        assertEquals("Settings", route.getName());
        assertEquals("/admin/settings", route.getPath());
    }
}
