package fr.tiogars.starter.feature.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import fr.tiogars.starter.feature.models.Feature;
import fr.tiogars.starter.feature.repositories.FeatureRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FeatureImportServiceBranchTest {

    @Autowired
    private FeatureImportService featureImportService;

    @Autowired
    private FeatureRepository featureRepository;

    @BeforeEach
    void setUp() {
        featureRepository.deleteAll();
    }

    @Test
    void shouldImportFeaturesFromBackupXmlFormat() throws JsonProcessingException {
        // Test backup XML format with proper features
        String xmlContent = """
                <backup>
                    <features>
                        <feature>
                            <name>Feature1</name>
                            <description>Desc 1</description>
                            <tag>tag1</tag>
                        </feature>
                        <feature>
                            <name>Feature2</name>
                            <description>Desc 2</description>
                            <tag>tag2</tag>
                        </feature>
                    </features>
                </backup>
                """;

        int result = featureImportService.importXml(xmlContent);

        assertEquals(2, result);
        assertEquals(2, featureRepository.count());
    }

    @Test
    void shouldFallbackToPlainFeaturesXmlWhenBackupEmpty() throws JsonProcessingException {
        // Test fallback to plain <features> root when backup format empty
        String xmlContent = """
                <features>
                    <feature>
                        <name>Feature1</name>
                        <description>Desc 1</description>
                        <tag>tag1</tag>
                    </feature>
                </features>
                """;

        int result = featureImportService.importXml(xmlContent);

        assertEquals(1, result);
        assertEquals(1, featureRepository.count());
    }

    @Test
    void shouldReturnZeroWhenEmptyXmlContent() throws JsonProcessingException {
        // Test empty XML content
        String xmlContent = "<features></features>";

        int result = featureImportService.importXml(xmlContent);

        assertEquals(0, result);
    }

    @Test
    void shouldCountOnlySuccessfullyCreatedFeatures() throws JsonProcessingException {
        // Test counting only features with non-null ID (successful creation)
        String xmlContent = """
                <features>
                    <feature>
                        <name>ValidFeature</name>
                        <description>Valid</description>
                        <tag>tag1</tag>
                    </feature>
                </features>
                """;

        int result = featureImportService.importXml(xmlContent);

        assertEquals(1, result);
        assertEquals(1, featureRepository.count());
    }
}
