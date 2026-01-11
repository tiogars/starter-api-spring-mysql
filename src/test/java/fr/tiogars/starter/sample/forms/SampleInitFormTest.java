package fr.tiogars.starter.sample.forms;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SampleInitFormTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleInitForm form = new SampleInitForm();

        // Assert
        assertNotNull(form);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleInitForm form = new SampleInitForm();
        Integer numberOfSamples = 10;

        // Act
        form.setNumberOfSamples(numberOfSamples);

        // Assert
        assertEquals(numberOfSamples, form.getNumberOfSamples());
    }

    @Test
    void testWithMinValue() {
        // Arrange
        SampleInitForm form = new SampleInitForm();

        // Act
        form.setNumberOfSamples(1);

        // Assert
        assertEquals(1, form.getNumberOfSamples());
    }

    @Test
    void testWithMaxValue() {
        // Arrange
        SampleInitForm form = new SampleInitForm();

        // Act
        form.setNumberOfSamples(100);

        // Assert
        assertEquals(100, form.getNumberOfSamples());
    }

    @Test
    void testWithTypicalValue() {
        // Arrange
        SampleInitForm form = new SampleInitForm();

        // Act
        form.setNumberOfSamples(50);

        // Assert
        assertEquals(50, form.getNumberOfSamples());
    }

    @Test
    void testWithNullValue() {
        // Arrange
        SampleInitForm form = new SampleInitForm();

        // Act
        form.setNumberOfSamples(null);

        // Assert
        assertNull(form.getNumberOfSamples());
    }
}
