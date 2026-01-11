package fr.tiogars.starter.sample.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SampleImportReportTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleImportReport report = new SampleImportReport();

        // Assert
        assertNotNull(report);
        assertNotNull(report.getItems());
        assertTrue(report.getItems().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleImportReport report = new SampleImportReport();
        int totalProvided = 10;
        int totalCreated = 7;
        int totalDuplicates = 2;
        int totalErrors = 1;
        int totalSkipped = 0;
        String alertLevel = "success";
        String message = "Import completed successfully";

        // Act
        report.setTotalProvided(totalProvided);
        report.setTotalCreated(totalCreated);
        report.setTotalDuplicates(totalDuplicates);
        report.setTotalErrors(totalErrors);
        report.setTotalSkipped(totalSkipped);
        report.setAlertLevel(alertLevel);
        report.setMessage(message);

        // Assert
        assertEquals(totalProvided, report.getTotalProvided());
        assertEquals(totalCreated, report.getTotalCreated());
        assertEquals(totalDuplicates, report.getTotalDuplicates());
        assertEquals(totalErrors, report.getTotalErrors());
        assertEquals(totalSkipped, report.getTotalSkipped());
        assertEquals(alertLevel, report.getAlertLevel());
        assertEquals(message, report.getMessage());
    }

    @Test
    void testAddItem() {
        // Arrange
        SampleImportReport report = new SampleImportReport();
        SampleImportReportItem item = new SampleImportReportItem("Sample1", true, "Created", "success");

        // Act
        report.addItem(item);

        // Assert
        assertEquals(1, report.getItems().size());
        assertEquals(item, report.getItems().get(0));
    }

    @Test
    void testSetItems() {
        // Arrange
        SampleImportReport report = new SampleImportReport();
        List<SampleImportReportItem> items = new ArrayList<>();
        items.add(new SampleImportReportItem("Sample1", true, "Created", "success"));
        items.add(new SampleImportReportItem("Sample2", false, "Duplicate", "warning"));

        // Act
        report.setItems(items);

        // Assert
        assertEquals(2, report.getItems().size());
    }

    @Test
    void testWithMultipleItems() {
        // Arrange
        SampleImportReport report = new SampleImportReport();

        // Act
        report.addItem(new SampleImportReportItem("Sample1", true, "Created", "success"));
        report.addItem(new SampleImportReportItem("Sample2", false, "Duplicate", "warning"));
        report.addItem(new SampleImportReportItem("Sample3", false, "Error", "error"));

        // Assert
        assertEquals(3, report.getItems().size());
    }

    @Test
    void testWithZeroTotals() {
        // Arrange
        SampleImportReport report = new SampleImportReport();

        // Act
        report.setTotalProvided(0);
        report.setTotalCreated(0);
        report.setTotalDuplicates(0);
        report.setTotalErrors(0);
        report.setTotalSkipped(0);

        // Assert
        assertEquals(0, report.getTotalProvided());
        assertEquals(0, report.getTotalCreated());
        assertEquals(0, report.getTotalDuplicates());
        assertEquals(0, report.getTotalErrors());
        assertEquals(0, report.getTotalSkipped());
    }
}
