package fr.tiogars.starter.route.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RouteEntityTest {

    @Test
    void testDefaultConstructor() {
        // Act
        RouteEntity entity = new RouteEntity();

        // Assert
        assertNotNull(entity);
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String name = "Home";
        String path = "/home";

        // Act
        RouteEntity entity = new RouteEntity(name, path);

        // Assert
        assertEquals(name, entity.getName());
        assertEquals(path, entity.getPath());
        assertNull(entity.getId());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        RouteEntity entity = new RouteEntity();
        Long id = 1L;
        String name = "Dashboard";
        String path = "/dashboard";

        // Act
        entity.setId(id);
        entity.setName(name);
        entity.setPath(path);

        // Assert
        assertEquals(id, entity.getId());
        assertEquals(name, entity.getName());
        assertEquals(path, entity.getPath());
    }

    @Test
    void testWithNullValues() {
        // Arrange
        RouteEntity entity = new RouteEntity();

        // Act
        entity.setId(null);
        entity.setName(null);
        entity.setPath(null);

        // Assert
        assertNull(entity.getId());
        assertNull(entity.getName());
        assertNull(entity.getPath());
    }

    @Test
    void testWithRootPath() {
        // Arrange
        RouteEntity entity = new RouteEntity();

        // Act
        entity.setName("Root");
        entity.setPath("/");

        // Assert
        assertEquals("Root", entity.getName());
        assertEquals("/", entity.getPath());
    }

    @Test
    void testWithNestedPath() {
        // Arrange
        RouteEntity entity = new RouteEntity();

        // Act
        entity.setName("Settings");
        entity.setPath("/admin/settings");

        // Assert
        assertEquals("Settings", entity.getName());
        assertEquals("/admin/settings", entity.getPath());
    }
}
