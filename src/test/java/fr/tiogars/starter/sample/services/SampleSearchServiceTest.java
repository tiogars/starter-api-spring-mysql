package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.models.FilterItem;
import fr.tiogars.starter.sample.models.FilterModel;
import fr.tiogars.starter.sample.models.SampleSearchRequest;
import fr.tiogars.starter.sample.models.SampleSearchResponse;
import fr.tiogars.starter.sample.models.SortItem;
import fr.tiogars.starter.sample.repositories.SampleRepository;

@ExtendWith(MockitoExtension.class)
class SampleSearchServiceTest {

    @Mock
    private SampleRepository sampleRepository;

    @InjectMocks
    private SampleSearchService sampleSearchService;

    private SampleEntity sampleEntity1;
    private SampleEntity sampleEntity2;
    private Date testDate;

    @BeforeEach
    void setUp() {
        testDate = new Date();
        
        sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(1L);
        sampleEntity1.setName("Sample 1");
        sampleEntity1.setDescription("Description 1");
        sampleEntity1.setActive(true);
        sampleEntity1.setCreatedAt(testDate);
        sampleEntity1.setCreatedBy("user1");
        sampleEntity1.setUpdatedAt(testDate);
        sampleEntity1.setUpdatedBy("user1");

        sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(2L);
        sampleEntity2.setName("Sample 2");
        sampleEntity2.setDescription("Description 2");
        sampleEntity2.setActive(false);
        sampleEntity2.setCreatedAt(testDate);
        sampleEntity2.setCreatedBy("user2");
        sampleEntity2.setUpdatedAt(testDate);
        sampleEntity2.setUpdatedBy("user2");
    }

    @Test
    void testSearch_WithoutFiltersOrSorting_ReturnsAllSamples() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        SampleSearchResponse response = sampleSearchService.search(request);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
        assertEquals(2, response.getRows().size());
        assertEquals("Sample 1", response.getRows().get(0).getName());
        assertEquals("Sample 2", response.getRows().get(1).getName());
        verify(sampleRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testSearch_WithPagination_ReturnsCorrectPage() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        request.setPage(1);
        request.setPageSize(1);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(1).withPage(1), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        SampleSearchResponse response = sampleSearchService.search(request);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
        assertEquals(1, response.getRows().size());
        assertEquals("Sample 2", response.getRows().get(0).getName());
    }

    @Test
    void testSearch_WithSorting_AppliesSortCorrectly() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        SortItem sortItem = new SortItem();
        sortItem.setField("name");
        sortItem.setSort("asc");
        request.setSortModel(Arrays.asList(sortItem));
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        SampleSearchResponse response = sampleSearchService.search(request);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
        verify(sampleRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testSearch_WithStringFilter_AppliesFilterCorrectly() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("name");
        filterItem.setOperator("contains");
        filterItem.setValue("Sample");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        SampleSearchResponse response = sampleSearchService.search(request);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
        verify(sampleRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testSearch_WithBooleanFilter_AppliesFilterCorrectly() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("active");
        filterItem.setOperator("equals");
        filterItem.setValue(true);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        SampleSearchResponse response = sampleSearchService.search(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
        assertEquals(1, response.getRows().size());
        assertTrue(response.getRows().get(0).isActive());
    }

    @Test
    void testSearch_WithNumericFilter_AppliesFilterCorrectly() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("id");
        filterItem.setOperator("=");
        filterItem.setValue(1);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        SampleSearchResponse response = sampleSearchService.search(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
        assertEquals(1, response.getRows().size());
        assertEquals(1L, response.getRows().get(0).getId());
    }

    @Test
    void testSearch_WithMultipleFiltersAndLogicOperator_AppliesFiltersCorrectly() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem1 = new FilterItem();
        filterItem1.setField("name");
        filterItem1.setOperator("contains");
        filterItem1.setValue("Sample");
        
        FilterItem filterItem2 = new FilterItem();
        filterItem2.setField("active");
        filterItem2.setOperator("equals");
        filterItem2.setValue(true);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem1, filterItem2));
        filterModel.setLogicOperator("and");
        request.setFilterModel(filterModel);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        SampleSearchResponse response = sampleSearchService.search(request);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
        assertEquals(1, response.getRows().size());
        verify(sampleRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testSearch_WithEmptyResults_ReturnsEmptyResponse() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        
        Page<SampleEntity> page = new PageImpl<>(Arrays.asList(), Pageable.ofSize(10), 0);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        SampleSearchResponse response = sampleSearchService.search(request);

        // Assert
        assertNotNull(response);
        assertEquals(0, response.getRowCount());
        assertEquals(0, response.getRows().size());
    }

    @Test
    void testSearch_WithDescendingSorting_AppliesSortCorrectly() {
        // Arrange
        SampleSearchRequest request = new SampleSearchRequest();
        SortItem sortItem = new SortItem();
        sortItem.setField("id");
        sortItem.setSort("desc");
        request.setSortModel(Arrays.asList(sortItem));
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity2, sampleEntity1);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        // Act
        SampleSearchResponse response = sampleSearchService.search(request);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
        assertEquals(2, response.getRows().size());
    }
}
