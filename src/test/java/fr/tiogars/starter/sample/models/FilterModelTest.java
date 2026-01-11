package fr.tiogars.starter.sample.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class FilterModelTest {

    @Test
    void testDefaultConstructor() {
        // Act
        FilterModel filterModel = new FilterModel();

        // Assert
        assertNotNull(filterModel);
        assertEquals("and", filterModel.getLogicOperator());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        FilterModel filterModel = new FilterModel();
        List<FilterItem> items = new ArrayList<>();
        FilterItem item1 = new FilterItem();
        item1.setField("name");
        item1.setOperator("contains");
        item1.setValue("test");
        items.add(item1);

        String logicOperator = "or";

        // Act
        filterModel.setItems(items);
        filterModel.setLogicOperator(logicOperator);

        // Assert
        assertEquals(items, filterModel.getItems());
        assertEquals(logicOperator, filterModel.getLogicOperator());
        assertEquals(1, filterModel.getItems().size());
    }

    @Test
    void testWithNullItems() {
        // Arrange & Act
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(null);

        // Assert
        assertNull(filterModel.getItems());
    }

    @Test
    void testWithEmptyItems() {
        // Arrange
        FilterModel filterModel = new FilterModel();
        List<FilterItem> emptyItems = new ArrayList<>();

        // Act
        filterModel.setItems(emptyItems);

        // Assert
        assertNotNull(filterModel.getItems());
        assertTrue(filterModel.getItems().isEmpty());
    }

    @Test
    void testWithMultipleItems() {
        // Arrange
        FilterModel filterModel = new FilterModel();
        List<FilterItem> items = new ArrayList<>();
        
        FilterItem item1 = new FilterItem();
        item1.setField("name");
        item1.setOperator("contains");
        item1.setValue("test");
        
        FilterItem item2 = new FilterItem();
        item2.setField("active");
        item2.setOperator("is");
        item2.setValue(true);
        
        items.add(item1);
        items.add(item2);

        // Act
        filterModel.setItems(items);
        filterModel.setLogicOperator("and");

        // Assert
        assertEquals(2, filterModel.getItems().size());
        assertEquals("and", filterModel.getLogicOperator());
    }

    @Test
    void testDefaultLogicOperatorValue() {
        // Act
        FilterModel filterModel = new FilterModel();

        // Assert
        assertEquals("and", filterModel.getLogicOperator());
    }
}
