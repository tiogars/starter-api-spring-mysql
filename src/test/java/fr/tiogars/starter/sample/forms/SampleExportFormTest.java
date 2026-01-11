package fr.tiogars.starter.sample.forms;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.tiogars.starter.sample.models.SampleSearchRequest;

class SampleExportFormTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleExportForm form = new SampleExportForm();

        // Assert
        assertNotNull(form);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleExportForm form = new SampleExportForm();
        String format = "xlsx";
        boolean zip = true;
        SampleSearchRequest searchRequest = new SampleSearchRequest();

        // Act
        form.setFormat(format);
        form.setZip(zip);
        form.setSearchRequest(searchRequest);

        // Assert
        assertEquals(format, form.getFormat());
        assertTrue(form.isZip());
        assertEquals(searchRequest, form.getSearchRequest());
    }

    @Test
    void testWithXlsxFormat() {
        // Arrange
        SampleExportForm form = new SampleExportForm();

        // Act
        form.setFormat("xlsx");

        // Assert
        assertEquals("xlsx", form.getFormat());
    }

    @Test
    void testWithCsvFormat() {
        // Arrange
        SampleExportForm form = new SampleExportForm();

        // Act
        form.setFormat("csv");

        // Assert
        assertEquals("csv", form.getFormat());
    }

    @Test
    void testWithXmlFormat() {
        // Arrange
        SampleExportForm form = new SampleExportForm();

        // Act
        form.setFormat("xml");

        // Assert
        assertEquals("xml", form.getFormat());
    }

    @Test
    void testWithJsonFormat() {
        // Arrange
        SampleExportForm form = new SampleExportForm();

        // Act
        form.setFormat("json");

        // Assert
        assertEquals("json", form.getFormat());
    }

    @Test
    void testIsZipDefaultValue() {
        // Arrange & Act
        SampleExportForm form = new SampleExportForm();

        // Assert
        assertFalse(form.isZip());
    }

    @Test
    void testWithNullSearchRequest() {
        // Arrange
        SampleExportForm form = new SampleExportForm();

        // Act
        form.setSearchRequest(null);

        // Assert
        assertNull(form.getSearchRequest());
    }
}
