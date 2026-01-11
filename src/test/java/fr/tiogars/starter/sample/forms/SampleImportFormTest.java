package fr.tiogars.starter.sample.forms;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SampleImportFormTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleImportForm form = new SampleImportForm();

        // Assert
        assertNotNull(form);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleImportForm form = new SampleImportForm();
        List<SampleCreateForm> samples = new ArrayList<>();
        SampleCreateForm createForm = new SampleCreateForm();
        createForm.setName("Sample1");
        samples.add(createForm);

        // Act
        form.setSamples(samples);

        // Assert
        assertEquals(samples, form.getSamples());
        assertEquals(1, form.getSamples().size());
    }

    @Test
    void testWithEmptyList() {
        // Arrange
        SampleImportForm form = new SampleImportForm();
        List<SampleCreateForm> emptySamples = new ArrayList<>();

        // Act
        form.setSamples(emptySamples);

        // Assert
        assertNotNull(form.getSamples());
        assertTrue(form.getSamples().isEmpty());
    }

    @Test
    void testWithMultipleSamples() {
        // Arrange
        SampleImportForm form = new SampleImportForm();
        List<SampleCreateForm> samples = new ArrayList<>();
        
        SampleCreateForm form1 = new SampleCreateForm();
        form1.setName("Sample1");
        
        SampleCreateForm form2 = new SampleCreateForm();
        form2.setName("Sample2");
        
        samples.add(form1);
        samples.add(form2);

        // Act
        form.setSamples(samples);

        // Assert
        assertEquals(2, form.getSamples().size());
    }

    @Test
    void testWithNullSamples() {
        // Arrange
        SampleImportForm form = new SampleImportForm();

        // Act
        form.setSamples(null);

        // Assert
        assertNull(form.getSamples());
    }
}
