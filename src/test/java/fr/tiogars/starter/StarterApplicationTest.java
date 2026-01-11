package fr.tiogars.starter;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StarterApplicationTest {

    @Test
    void testApplicationClassExists() {
        // This test ensures the StarterApplication class can be loaded
        // Act & Assert
        assertNotNull(StarterApplication.class);
    }

    @Test
    void testMainMethodExists() {
        // Verify that main method exists
        try {
            StarterApplication.class.getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            fail("Main method should exist");
        }
    }
}
