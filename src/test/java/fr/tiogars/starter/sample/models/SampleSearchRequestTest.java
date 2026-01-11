package fr.tiogars.starter.sample.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SampleSearchRequestTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SampleSearchRequest request = new SampleSearchRequest();

        // Assert
        assertNotNull(request);
        assertEquals(0, request.getPage());
        assertEquals(10, request.getPageSize());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        int page = 2;
        int pageSize = 25;
        List<SortItem> sortModel = new ArrayList<>();
        SortItem sortItem = new SortItem();
        sortItem.setField("name");
        sortItem.setSort("asc");
        sortModel.add(sortItem);
        
        FilterModel filterModel = new FilterModel();

        // Act
        request.setPage(page);
        request.setPageSize(pageSize);
        request.setSortModel(sortModel);
        request.setFilterModel(filterModel);

        // Assert
        assertEquals(page, request.getPage());
        assertEquals(pageSize, request.getPageSize());
        assertEquals(sortModel, request.getSortModel());
        assertEquals(filterModel, request.getFilterModel());
    }

    @Test
    void testDefaultPageValue() {
        // Act
        SampleSearchRequest request = new SampleSearchRequest();

        // Assert
        assertEquals(0, request.getPage());
    }

    @Test
    void testDefaultPageSizeValue() {
        // Act
        SampleSearchRequest request = new SampleSearchRequest();

        // Assert
        assertEquals(10, request.getPageSize());
    }

    @Test
    void testWithNullSortModel() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();

        // Act
        request.setSortModel(null);

        // Assert
        assertNull(request.getSortModel());
    }

    @Test
    void testWithNullFilterModel() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();

        // Act
        request.setFilterModel(null);

        // Assert
        assertNull(request.getFilterModel());
    }

    @Test
    void testWithCustomPageSize() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();

        // Act
        request.setPageSize(50);

        // Assert
        assertEquals(50, request.getPageSize());
    }

    @Test
    void testWithMultipleSortItems() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        List<SortItem> sortModel = new ArrayList<>();
        
        SortItem sort1 = new SortItem();
        sort1.setField("name");
        sort1.setSort("asc");
        
        SortItem sort2 = new SortItem();
        sort2.setField("createdAt");
        sort2.setSort("desc");
        
        sortModel.add(sort1);
        sortModel.add(sort2);

        // Act
        request.setSortModel(sortModel);

        // Assert
        assertEquals(2, request.getSortModel().size());
    }
}
