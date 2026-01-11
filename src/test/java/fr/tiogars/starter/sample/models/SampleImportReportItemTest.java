package fr.tiogars.starter.sample.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SampleImportReportItemTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleImportReportItem item = new SampleImportReportItem();

        // Assert
        assertNotNull(item);
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String name = "TestSample";
        boolean created = true;
        String message = "Created successfully";
        String alertLevel = "success";

        // Act
        SampleImportReportItem item = new SampleImportReportItem(name, created, message, alertLevel);

        // Assert
        assertEquals(name, item.getName());
        assertTrue(item.isCreated());
        assertEquals(message, item.getMessage());
        assertEquals(alertLevel, item.getAlertLevel());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleImportReportItem item = new SampleImportReportItem();
        String name = "Sample1";
        boolean created = false;
        String message = "Duplicate entry";
        String alertLevel = "warning";

        // Act
        item.setName(name);
        item.setCreated(created);
        item.setMessage(message);
        item.setAlertLevel(alertLevel);

        // Assert
        assertEquals(name, item.getName());
        assertFalse(item.isCreated());
        assertEquals(message, item.getMessage());
        assertEquals(alertLevel, item.getAlertLevel());
    }

    @Test
    void testWithSuccessfulCreation() {
        // Arrange & Act
        SampleImportReportItem item = new SampleImportReportItem("NewSample", true, "Created", "success");

        // Assert
        assertEquals("NewSample", item.getName());
        assertTrue(item.isCreated());
        assertEquals("Created", item.getMessage());
        assertEquals("success", item.getAlertLevel());
    }

    @Test
    void testWithErrorAlert() {
        // Arrange & Act
        SampleImportReportItem item = new SampleImportReportItem("FailedSample", false, "Validation error", "error");

        // Assert
        assertEquals("FailedSample", item.getName());
        assertFalse(item.isCreated());
        assertEquals("Validation error", item.getMessage());
        assertEquals("error", item.getAlertLevel());
    }

    @Test
    void testWithWarningAlert() {
        // Arrange & Act
        SampleImportReportItem item = new SampleImportReportItem("DupeSample", false, "Already exists", "warning");

        // Assert
        assertEquals("DupeSample", item.getName());
        assertFalse(item.isCreated());
        assertEquals("Already exists", item.getMessage());
        assertEquals("warning", item.getAlertLevel());
    }
}
