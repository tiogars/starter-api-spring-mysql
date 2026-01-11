package fr.tiogars.starter.sample.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SortItemTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SortItem sortItem = new SortItem();

        // Assert
        assertNotNull(sortItem);
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SortItem sortItem = new SortItem();
        String field = "name";
        String sort = "asc";

        // Act
        sortItem.setField(field);
        sortItem.setSort(sort);

        // Assert
        assertEquals(field, sortItem.getField());
        assertEquals(sort, sortItem.getSort());
    }

    @Test
    void testWithNullValues() {
        // Arrange & Act
        SortItem sortItem = new SortItem();
        sortItem.setField(null);
        sortItem.setSort(null);

        // Assert
        assertNull(sortItem.getField());
        assertNull(sortItem.getSort());
    }

    @Test
    void testWithDescendingSort() {
        // Arrange
        SortItem sortItem = new SortItem();

        // Act
        sortItem.setField("createdAt");
        sortItem.setSort("desc");

        // Assert
        assertEquals("createdAt", sortItem.getField());
        assertEquals("desc", sortItem.getSort());
    }

    @Test
    void testWithAscendingSort() {
        // Arrange
        SortItem sortItem = new SortItem();

        // Act
        sortItem.setField("name");
        sortItem.setSort("asc");

        // Assert
        assertEquals("name", sortItem.getField());
        assertEquals("asc", sortItem.getSort());
    }
}
