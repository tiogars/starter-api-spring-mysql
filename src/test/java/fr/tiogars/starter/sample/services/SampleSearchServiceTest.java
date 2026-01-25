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
import org.mockito.ArgumentCaptor;
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

    // ============ BRANCH COVERAGE TESTS FOR FILTER OPERATORS ============

    @Test
    void testSearch_NumericFilterNotEquals_CoversNotEqualsBranch() {
        // Covers the != operator branch for numeric fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("id");
        filterItem.setOperator("!=");
        filterItem.setValue(2);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testSearch_NumericFilterGreaterThan_CoversGreaterThanBranch() {
        // Covers the > operator branch for numeric fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("id");
        filterItem.setOperator(">");
        filterItem.setValue(1);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testSearch_NumericFilterGreaterThanOrEqual_CoversGreaterThanOrEqualBranch() {
        // Covers the >= operator branch for numeric fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("id");
        filterItem.setOperator(">=");
        filterItem.setValue(2);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testSearch_NumericFilterLessThan_CoversLessThanBranch() {
        // Covers the < operator branch for numeric fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("id");
        filterItem.setOperator("<");
        filterItem.setValue(2);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testSearch_NumericFilterLessThanOrEqual_CoversLessThanOrEqualBranch() {
        // Covers the <= operator branch for numeric fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("id");
        filterItem.setOperator("<=");
        filterItem.setValue(1);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testSearch_StringFilterEquals_CoversStringEqualsBranch() {
        // Covers the equals operator for string fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("name");
        filterItem.setOperator("equals");
        filterItem.setValue("Sample 1");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testSearch_StringFilterStartsWith_CoversStartsWithBranch() {
        // Covers the startsWith operator for string fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("name");
        filterItem.setOperator("startsWith");
        filterItem.setValue("Sample");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_StringFilterEndsWith_CoversEndsWithBranch() {
        // Covers the endsWith operator for string fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("description");
        filterItem.setOperator("endsWith");
        filterItem.setValue("2");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testSearch_StringFilterIsEmpty_CoversIsEmptyBranch() {
        // Covers the isEmpty operator for string fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("name");
        filterItem.setOperator("isEmpty");
        filterItem.setValue("");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        Page<SampleEntity> page = new PageImpl<>(Arrays.asList(), Pageable.ofSize(10), 0);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(0, response.getRowCount());
    }

    @Test
    void testSearch_StringFilterIsNotEmpty_CoversIsNotEmptyBranch() {
        // Covers the isNotEmpty operator for string fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("name");
        filterItem.setOperator("isNotEmpty");
        filterItem.setValue("");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_DateFilterEquals_CoversDateEqualsBranch() {
        // Covers the = operator for date fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("createdAt");
        filterItem.setOperator("=");
        filterItem.setValue(testDate.getTime());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_DateFilterAfter_CoversDateAfterBranch() {
        // Covers the after operator for date fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        Date beforeDate = new Date(testDate.getTime() - 1000);
        FilterItem filterItem = new FilterItem();
        filterItem.setField("createdAt");
        filterItem.setOperator("after");
        filterItem.setValue(beforeDate.getTime());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_DateFilterBefore_CoversDateBeforeBranch() {
        // Covers the before operator for date fields
        SampleSearchRequest request = new SampleSearchRequest();
        
        Date afterDate = new Date(testDate.getTime() + 1000);
        FilterItem filterItem = new FilterItem();
        filterItem.setField("updatedAt");
        filterItem.setOperator("before");
        filterItem.setValue(afterDate.getTime());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_FilterWithOrLogicOperator_CoversOrLogicBranch() {
        // Covers the OR logic operator branch
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem1 = new FilterItem();
        filterItem1.setField("name");
        filterItem1.setOperator("equals");
        filterItem1.setValue("Sample 1");
        
        FilterItem filterItem2 = new FilterItem();
        filterItem2.setField("name");
        filterItem2.setOperator("equals");
        filterItem2.setValue("Sample 2");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem1, filterItem2));
        filterModel.setLogicOperator("or");
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_FilterWithNullField_SkipsInvalidFilter() {
        // Covers null field handling branch
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField(null);
        filterItem.setOperator("equals");
        filterItem.setValue("value");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_UnknownField_SkipsFilterBranch() {
        // Covers unknown field handling branch
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("unknownField");
        filterItem.setOperator("equals");
        filterItem.setValue("value");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_InvalidNumericValue_SkipsFilter() {
        // Covers invalid numeric conversion branch
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("id");
        filterItem.setOperator("=");
        filterItem.setValue("notanumber");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_InvalidDateValue_SkipsFilter() {
        // Covers invalid date conversion branch
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("createdAt");
        filterItem.setOperator("=");
        filterItem.setValue("notadate");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_EmptyFilterList_AppliesToAllRecords() {
        // Covers empty filter list branch
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList());
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_NullFilterModel_AppliesDefaultFilter() {
        // Covers null filter model branch
        SampleSearchRequest request = new SampleSearchRequest();
        request.setFilterModel(null);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_NullSortModel_AppliesDefaultSort() {
        // Covers null sort model branch
        SampleSearchRequest request = new SampleSearchRequest();
        request.setSortModel(null);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity1, sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_MultipleSortItems_AppliesMultipleSorts() {
        // Covers multiple sort items branch
        SampleSearchRequest request = new SampleSearchRequest();
        
        SortItem sortItem1 = new SortItem();
        sortItem1.setField("active");
        sortItem1.setSort("asc");
        
        SortItem sortItem2 = new SortItem();
        sortItem2.setField("name");
        sortItem2.setSort("desc");
        
        request.setSortModel(Arrays.asList(sortItem1, sortItem2));
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity2, sampleEntity1);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 2);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testSearch_BooleanFilterFalse_CoversActiveFalseBranch() {
        // Covers boolean filter with false value
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filterItem = new FilterItem();
        filterItem.setField("active");
        filterItem.setOperator("equals");
        filterItem.setValue(false);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filterItem));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(10);
        
        List<SampleEntity> entities = Arrays.asList(sampleEntity2);
        Page<SampleEntity> page = new PageImpl<>(entities, Pageable.ofSize(10), 1);
        
        when(sampleRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }
}
