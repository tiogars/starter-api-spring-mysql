package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.models.FilterItem;
import fr.tiogars.starter.sample.models.FilterModel;
import fr.tiogars.starter.sample.models.SampleSearchRequest;
import fr.tiogars.starter.sample.models.SampleSearchResponse;
import fr.tiogars.starter.sample.repositories.SampleRepository;

/**
 * Integration tests for SampleSearchService using real repository and database.
 * Tests exercise actual branch coverage in Specification predicate building.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class SampleSearchServiceIntegrationTest {

    @Autowired
    private SampleSearchService sampleSearchService;

    @Autowired
    private SampleRepository sampleRepository;

    private SampleEntity entity1;
    private SampleEntity entity2;
    private SampleEntity entity3;
    private Date testDate;
    private Date futureDate;

    @BeforeEach
    void setUp() {
        // Clear repository
        sampleRepository.deleteAll();
        
        testDate = new Date(System.currentTimeMillis() - 86400000); // 1 day ago
        futureDate = new Date(System.currentTimeMillis() + 86400000); // 1 day from now
        
        // Create test entities with distinct values (let DB auto-generate IDs)
        entity1 = new SampleEntity();
        entity1.setName("Alpha");
        entity1.setDescription("First sample");
        entity1.setActive(true);
        entity1.setCreatedAt(testDate);
        entity1.setCreatedBy("admin");
        entity1.setUpdatedAt(testDate);
        entity1.setUpdatedBy("admin");
        entity1 = sampleRepository.save(entity1);

        entity2 = new SampleEntity();
        entity2.setName("Beta");
        entity2.setDescription("Second sample");
        entity2.setActive(false);
        entity2.setCreatedAt(testDate);
        entity2.setCreatedBy("user");
        entity2.setUpdatedAt(testDate);
        entity2.setUpdatedBy("user");
        entity2 = sampleRepository.save(entity2);

        entity3 = new SampleEntity();
        entity3.setName("Gamma");
        entity3.setDescription("Third sample");
        entity3.setActive(true);
        entity3.setCreatedAt(futureDate);
        entity3.setCreatedBy("admin");
        entity3.setUpdatedAt(futureDate);
        entity3.setUpdatedBy("admin");
        entity3 = sampleRepository.save(entity3);
    }

    // ========== NUMERIC FIELD TESTS ==========

    @Test
    void testNumericEquals_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("=");
        filter.setValue(entity1.getId());
        
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
    void testNumericEqualsOperator_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("equals");
        filter.setValue(entity1.getId());
        
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
    void testNumericNotEqual_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("!=");
        filter.setValue(entity1.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testNumericNotOperator_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("not");
        filter.setValue(entity1.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testNumericGreaterThan_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator(">");
        // Use entity2.getId() - 1 to match entities 2 and 3
        filter.setValue(entity1.getId() + 0.5);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testNumericGreaterThanOperator_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("greaterThan");
        filter.setValue(entity1.getId() + 0.5);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testNumericGreaterThanOrEqual_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator(">=");
        filter.setValue(entity2.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testNumericGreaterThanOrEqualOperator_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("greaterThanOrEqual");
        filter.setValue(entity2.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testNumericLessThan_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("<");
        // entity3.getId() should be 3, so < 3 means IDs 1,2
        filter.setValue(entity3.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testNumericLessThanOperator_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("lessThan");
        filter.setValue(entity3.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testNumericLessThanOrEqual_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("<=");
        filter.setValue(entity2.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testNumericLessThanOrEqualOperator_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("id");
        filter.setOperator("lessThanOrEqual");
        filter.setValue(entity2.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    // ========== STRING FIELD TESTS ==========

    @Test
    void testStringContains_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("name");
        filter.setOperator("contains");
        // Only "Alpha" and "Gamma" contain "h"
        filter.setValue("h");
        
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
    void testStringEquals_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("name");
        filter.setOperator("equals");
        filter.setValue("Beta");
        
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
    void testStringStartsWith_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("name");
        filter.setOperator("startsWith");
        filter.setValue("Al");
        
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
    void testStringEndsWith_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("name");
        filter.setOperator("endsWith");
        // Only "Gamma" ends with "mma"
        filter.setValue("mma");
        
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
    void testStringIsEmpty_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        // Use a field that is actually empty in the test data
        filter.setField("notes");
        filter.setOperator("isEmpty");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // All 3 entities should have empty/null notes field
        assertEquals(3, response.getRowCount());
    }

    @Test
    void testStringIsNotEmpty_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("description");
        filter.setOperator("isNotEmpty");
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        // All 3 entities have descriptions
        assertEquals(3, response.getRowCount());
    }

    // ========== BOOLEAN FIELD TESTS ==========

    @Test
    void testBooleanTrue_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("active");
        filter.setOperator("equals");
        filter.setValue(true);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testBooleanFalse_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("active");
        filter.setOperator("equals");
        filter.setValue(false);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(1, response.getRowCount());
    }

    // ========== DATE FIELD TESTS ==========

    @Test
    void testDateEquals_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("is");
        filter.setValue(testDate.getTime());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testDateEqualsOperator_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("equals");
        filter.setValue(testDate.getTime());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testDateNotEqual_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("not");
        filter.setValue(testDate.getTime());
        
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
    void testDateAfter_CoversBranch() {
        long midpointTime = testDate.getTime() + 43200000; // 12 hours after testDate
        
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("after");
        filter.setValue(midpointTime);
        
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
    void testDateGreaterThan_CoversBranch() {
        long midpointTime = testDate.getTime() + 43200000;
        
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator(">");
        filter.setValue(midpointTime);
        
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
    void testDateOnOrAfter_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("onOrAfter");
        filter.setValue(testDate.getTime());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(3, response.getRowCount());
    }

    @Test
    void testDateGreaterThanOrEqual_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator(">=");
        filter.setValue(testDate.getTime());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(3, response.getRowCount());
    }

    @Test
    void testDateBefore_CoversBranch() {
        long midpointTime = testDate.getTime() + 43200000;
        
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("before");
        filter.setValue(midpointTime);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testDateLessThan_CoversBranch() {
        long midpointTime = testDate.getTime() + 43200000;
        
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("<");
        filter.setValue(midpointTime);
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testDateOnOrBefore_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("onOrBefore");
        filter.setValue(testDate.getTime());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    @Test
    void testDateLessThanOrEqual_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        FilterItem filter = new FilterItem();
        filter.setField("createdAt");
        filter.setOperator("<=");
        filter.setValue(testDate.getTime());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter));
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }

    // ========== LOGIC OPERATOR TESTS ==========

    @Test
    void testAndLogicOperator_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filter1 = new FilterItem();
        filter1.setField("active");
        filter1.setOperator("equals");
        filter1.setValue(true);
        
        FilterItem filter2 = new FilterItem();
        filter2.setField("id");
        filter2.setOperator("=");
        filter2.setValue(entity1.getId());
        
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
    void testOrLogicOperator_CoversBranch() {
        SampleSearchRequest request = new SampleSearchRequest();
        
        FilterItem filter1 = new FilterItem();
        filter1.setField("id");
        filter1.setOperator("=");
        filter1.setValue(entity1.getId());
        
        FilterItem filter2 = new FilterItem();
        filter2.setField("id");
        filter2.setOperator("=");
        filter2.setValue(entity2.getId());
        
        FilterModel filterModel = new FilterModel();
        filterModel.setItems(Arrays.asList(filter1, filter2));
        filterModel.setLogicOperator("or");
        request.setFilterModel(filterModel);
        request.setPage(0);
        request.setPageSize(20);
        
        SampleSearchResponse response = sampleSearchService.search(request);
        assertNotNull(response);
        assertEquals(2, response.getRowCount());
    }
}
