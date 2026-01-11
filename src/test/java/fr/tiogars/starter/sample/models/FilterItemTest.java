package fr.tiogars.starter.sample.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FilterItemTest {

    @Test
    void testDefaultConstructor() {
        // Act
        FilterItem filterItem = new FilterItem();

        // Assert
        assertNotNull(filterItem);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        FilterItem filterItem = new FilterItem();
        String field = "name";
        String operator = "contains";
        Object value = "test";

        // Act
        filterItem.setField(field);
        filterItem.setOperator(operator);
        filterItem.setValue(value);

        // Assert
        assertEquals(field, filterItem.getField());
        assertEquals(operator, filterItem.getOperator());
        assertEquals(value, filterItem.getValue());
    }

    @Test
    void testWithNullValues() {
        // Arrange & Act
        FilterItem filterItem = new FilterItem();
        filterItem.setField(null);
        filterItem.setOperator(null);
        filterItem.setValue(null);

        // Assert
        assertNull(filterItem.getField());
        assertNull(filterItem.getOperator());
        assertNull(filterItem.getValue());
    }

    @Test
    void testWithNumericValue() {
        // Arrange
        FilterItem filterItem = new FilterItem();
        Integer numericValue = 123;

        // Act
        filterItem.setField("id");
        filterItem.setOperator("equals");
        filterItem.setValue(numericValue);

        // Assert
        assertEquals("id", filterItem.getField());
        assertEquals("equals", filterItem.getOperator());
        assertEquals(numericValue, filterItem.getValue());
    }

    @Test
    void testWithBooleanValue() {
        // Arrange
        FilterItem filterItem = new FilterItem();
        Boolean boolValue = true;

        // Act
        filterItem.setField("active");
        filterItem.setOperator("is");
        filterItem.setValue(boolValue);

        // Assert
        assertEquals("active", filterItem.getField());
        assertEquals("is", filterItem.getOperator());
        assertEquals(boolValue, filterItem.getValue());
    }
}
