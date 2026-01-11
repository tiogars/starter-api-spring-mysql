package fr.tiogars.starter.sample.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SampleSearchResponseTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleSearchResponse response = new SampleSearchResponse();

        // Assert
        assertNotNull(response);
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        List<Sample> rows = new ArrayList<>();
        Sample sample1 = new Sample();
        sample1.setName("Sample1");
        rows.add(sample1);
        
        long rowCount = 1L;

        // Act
        SampleSearchResponse response = new SampleSearchResponse(rows, rowCount);

        // Assert
        assertEquals(rows, response.getRows());
        assertEquals(rowCount, response.getRowCount());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleSearchResponse response = new SampleSearchResponse();
        List<Sample> rows = new ArrayList<>();
        Sample sample = new Sample();
        sample.setName("TestSample");
        rows.add(sample);
        
        long rowCount = 5L;

        // Act
        response.setRows(rows);
        response.setRowCount(rowCount);

        // Assert
        assertEquals(rows, response.getRows());
        assertEquals(rowCount, response.getRowCount());
        assertEquals(1, response.getRows().size());
    }

    @Test
    void testWithEmptyRows() {
        // Arrange
        List<Sample> emptyRows = new ArrayList<>();
        
        // Act
        SampleSearchResponse response = new SampleSearchResponse(emptyRows, 0L);

        // Assert
        assertNotNull(response.getRows());
        assertTrue(response.getRows().isEmpty());
        assertEquals(0L, response.getRowCount());
    }

    @Test
    void testWithMultipleRows() {
        // Arrange
        List<Sample> rows = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Sample sample = new Sample();
            sample.setName("Sample" + i);
            rows.add(sample);
        }

        // Act
        SampleSearchResponse response = new SampleSearchResponse(rows, 100L);

        // Assert
        assertEquals(3, response.getRows().size());
        assertEquals(100L, response.getRowCount());
    }

    @Test
    void testWithZeroRowCount() {
        // Arrange
        SampleSearchResponse response = new SampleSearchResponse();

        // Act
        response.setRowCount(0L);

        // Assert
        assertEquals(0L, response.getRowCount());
    }
}
