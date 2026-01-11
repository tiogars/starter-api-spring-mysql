package fr.tiogars.starter.route.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PageDefinitionControllerTest {

    @Test
    void testControllerInstantiation() {
        // Act
        PageDefinitionController controller = new PageDefinitionController();

        // Assert
        assertNotNull(controller);
    }
}
