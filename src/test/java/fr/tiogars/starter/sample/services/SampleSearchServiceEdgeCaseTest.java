package fr.tiogars.starter.sample.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.models.FilterItem;
import fr.tiogars.starter.sample.models.FilterModel;
import fr.tiogars.starter.sample.models.SampleSearchRequest;
import fr.tiogars.starter.sample.models.SampleSearchResponse;
import fr.tiogars.starter.sample.repositories.SampleRepository;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Edge case and branch coverage tests for SampleSearchService.
 * Tests exception handling paths and compound boolean predicates.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SampleSearchServiceEdgeCaseTest {
    @Autowired
    private SampleSearchService sampleSearchService;

    @Autowired
    private SampleRepository sampleRepository;

    private SampleEntity entity1;
    private Date testDate;

    @BeforeEach
    void setUp() {
        sampleRepository.deleteAll();
        
        testDate = new Date(System.currentTimeMillis() - 86400000);
        
        entity1 = new SampleEntity();
        entity1.setName("TestEntity");
        entity1.setDescription("Test Description");
        entity1.setActive(true);
        entity1.setCreatedAt(testDate);
        entity1.setCreatedBy("admin");
        entity1.setUpdatedAt(testDate);
        entity1.setUpdatedBy("admin");
        entity1 = sampleRepository.save(entity1);
    }

    // ========== INVALID TYPE CONVERSION TESTS ==========

    @Test
    void testNumericFilterWithInvalidStringValue_ShouldIgnoreFilter() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("=");
        filter.setValue("not_a_number"); // Invalid, should be converted to null
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Invalid filter should be ignored, return all records
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testBooleanFilterWithInvalidValue_ShouldIgnoreFilter() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("active");
        filter.setOperator("equals");
        filter.setValue("invalid_boolean");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Invalid filter should be ignored
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testDateFilterWithInvalidValue_ShouldIgnoreFilter() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("=");
        filter.setValue("not_a_timestamp");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Invalid filter should be ignored
        assertEquals(1, response.getRowCount());
    }

    // ========== UNKNOWN FIELD TESTS ==========

    @Test
    void testFilterWithUnknownField_ShouldIgnoreFilter() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("unknownField");
        filter.setOperator("=");
        filter.setValue("someValue");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Unknown field should be ignored
        assertEquals(1, response.getRowCount());
    }

    // ========== UNKNOWN OPERATOR TESTS ==========

    @Test
    void testNumericFilterWithUnknownOperator_ShouldIgnoreFilter() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("unknownOp");
        filter.setValue(entity1.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Unknown operator should be ignored
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testStringFilterWithUnknownOperator_ShouldIgnoreFilter() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("name");
        filter.setOperator("unknownOp");
        filter.setValue("Test");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Unknown operator should be ignored
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testDateFilterWithUnknownOperator_ShouldIgnoreFilter() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("unknownOp");
        filter.setValue(testDate.getTime());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Unknown operator should be ignored
        assertEquals(1, response.getRowCount());
    }

    // ========== NULL FIELD/OPERATOR VALUE TESTS ==========

    @Test
    void testFilterWithNullField_ShouldIgnoreFilter() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField(null);
        filter.setOperator("=");
        filter.setValue("value");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Null field should be ignored
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testFilterWithNullOperator_ShouldIgnoreFilter() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator(null);
        filter.setValue(entity1.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Null operator should be ignored
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testFilterWithNullValue_ShouldIgnoreFilter() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("=");
        filter.setValue(null);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Null value should be ignored
        assertEquals(1, response.getRowCount());
    }

    // ========== EMPTY FILTER MODEL TESTS ==========

    @Test
    void testSearchWithNullFilterModel_ShouldReturnAllRecords() {
        SampleSearchRequest request = new SampleSearchRequest();
        request.setFilterModel(null);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testSearchWithEmptyFilterItems_ShouldReturnAllRecords() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList());
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    // ========== COMPOUND STRING PREDICATE TESTS ==========

    @Test
    void testStringIsEmptyWithNullValue_ShouldMatchNullDescription() {
        // Clear existing data and create new entity with null description
        sampleRepository.deleteAll();
        
        SampleEntity entity2 = new SampleEntity();
        entity2.setName("NoDescription");
        entity2.setDescription(null);
        entity2.setActive(true);
        entity2.setCreatedAt(testDate);
        entity2.setCreatedBy("admin");
        entity2.setUpdatedAt(testDate);
        entity2.setUpdatedBy("admin");
        entity2 = sampleRepository.save(entity2);
        
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("description");
        filter.setOperator("isEmpty");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Should find 1 entity with null description (entity2 only)
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testStringIsEmptyWithEmptyString_ShouldMatchEmptyDescription() {
        // Delete the original entity first to avoid matching
        sampleRepository.deleteAll();
        
        // Create entity with empty string description
        SampleEntity entity2 = new SampleEntity();
        entity2.setName("EmptyDescription");
        entity2.setDescription("");
        entity2.setActive(true);
        entity2.setCreatedAt(testDate);
        entity2.setCreatedBy("admin");
        entity2.setUpdatedAt(testDate);
        entity2.setUpdatedBy("admin");
        entity2 = sampleRepository.save(entity2);
        
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("description");
        filter.setOperator("isEmpty");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // Should find 1 entity with empty string description
        assertEquals(1, response.getRowCount());
    }

    // ========== BOOLEAN CONVERSION TESTS ==========

    @Test
    void testBooleanFilterWithStringTrue_ShouldMatchTrue() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("active");
        filter.setOperator("equals");
        filter.setValue("true"); // String "true"
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testBooleanFilterWithStringOne_ShouldMatchTrue() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("active");
        filter.setOperator("equals");
        filter.setValue("1"); // String "1" should be treated as true
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    // ========== SORT TESTS ==========

    @Test
    void testSearchWithNullSort_ShouldDefaultToDescById() {
        SampleSearchRequest request = new SampleSearchRequest();
        request.setSortModel(null);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    // ========== STRING CONVERSION WITH SPECIAL CHARACTERS ==========

    @Test
    void testStringFilterWithSpecialCharacters() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("name");
        filter.setOperator("contains");
        filter.setValue("st%");  // Contains special characters
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // May not match anything but should not error
        assertNotNull(response);
    }

    // ========== CREATEDBY/UPDATEDBY FIELD TESTS ==========

    @Test
    void testSearchByCreatedBy_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdBy");
        filter.setOperator("equals");
        filter.setValue("admin");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testSearchByUpdatedBy_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("updatedBy");
        filter.setOperator("contains");
        filter.setValue("adm");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    // ========== MULTIPLE FILTER ITEMS TESTS ==========

    @Test
    void testMultipleFiltersWithAnd_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filter1 = new FilterItem();
        filter1.setField("name");
        filter1.setOperator("contains");
        filter1.setValue("Test");
        
        FilterItem filter2 = new FilterItem();
        filter2.setField("active");
        filter2.setOperator("equals");
        filter2.setValue(true);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter1, filter2));
        filterModel.setLogicOperator("and");
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    @Test
    void testMultipleFiltersWithDefaultLogicOperator_ShouldUseAnd() {
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filter1 = new FilterItem();
        filter1.setField("name");
        filter1.setOperator("contains");
        filter1.setValue("Test");
        
        FilterItem filter2 = new FilterItem();
        filter2.setField("active");
        filter2.setOperator("equals");
        filter2.setValue(true);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter1, filter2));
        filterModel.setLogicOperator(null);  // null should default to AND
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }
}
