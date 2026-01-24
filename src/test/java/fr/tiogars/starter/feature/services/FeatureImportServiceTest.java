package fr.tiogars.starter.feature.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.tiogars.starter.feature.models.Feature;

class FeatureImportServiceTest {

    private FeatureCreateService featureCreateService;
    private FeatureImportService featureImportService;

    @BeforeEach
    void setUp() {
        featureCreateService = mock(FeatureCreateService.class);
        featureImportService = new FeatureImportService(featureCreateService);

        Feature created = new Feature();
        created.setId(1L);
        when(featureCreateService.create(any())).thenReturn(created);
    }

    @Test
    void shouldImportFromBackupFileWithBackupRootAndTitle() throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
        // Load the provided backup XML from docs
        Path path = Path.of("docs", "features", "assets", "features-backup-2026-01-23T07-00-13-017Z.xml");
        String xml = Files.readString(path, StandardCharsets.UTF_8);

        int created = featureImportService.importXml(xml);

        assertTrue(created >= 1, "Expected at least one feature imported");
        verify(featureCreateService, atLeastOnce()).create(any());
    }

    @Test
    void testImportXml_PlainFeaturesRoot_Success() throws Exception {
        // Arrange
        String xml = """
            <features>
                <feature>
                    <name>Feature1</name>
                    <description>Description 1</description>
                    <tag>Tag1</tag>
                    <repository>Repo1</repository>
                    <app>App1</app>
                </feature>
                <feature>
                    <name>Feature2</name>
                    <description>Description 2</description>
                    <tag>Tag2</tag>
                    <repository>Repo2</repository>
                    <app>App2</app>
                </feature>
            </features>
            """;

        Feature created = new Feature();
        created.setId(1L);
        when(featureCreateService.create(any())).thenReturn(created);

        // Act
        int result = featureImportService.importXml(xml);

        // Assert
        assertTrue(result == 2, "Expected 2 features imported");
        verify(featureCreateService, atLeastOnce()).create(any());
    }

    @Test
    void testImportXml_EmptyXml_ReturnsZero() throws Exception {
        // Arrange
        String xml = "<features></features>";

        // Act
        int result = featureImportService.importXml(xml);

        // Assert
        assertTrue(result == 0, "Expected 0 features imported for empty XML");
    }

    @Test
    void testImportXml_NullFeaturesList_ReturnsZero() throws Exception {
        // Arrange
        String xml = "<backup></backup>";

        // Act
        int result = featureImportService.importXml(xml);

        // Assert
        assertTrue(result == 0, "Expected 0 features imported for null features list");
    }

    @Test
    void testImportXml_BackupRootWithFeatures_Success() throws Exception {
        // Arrange
        String xml = """
            <backup>
                <features>
                    <feature>
                        <name>BackupFeature</name>
                        <description>Backup Description</description>
                        <tag>BackupTag</tag>
                        <repository>BackupRepo</repository>
                        <app>BackupApp</app>
                    </feature>
                </features>
            </backup>
            """;

        Feature created = new Feature();
        created.setId(1L);
        when(featureCreateService.create(any())).thenReturn(created);

        // Act
        int result = featureImportService.importXml(xml);

        // Assert
        assertTrue(result == 1, "Expected 1 feature imported from backup XML");
        verify(featureCreateService, atLeastOnce()).create(any());
    }

    @Test
    void testImportXml_FeatureWithoutId_CountsAsCreated() throws Exception {
        // Arrange
        String xml = """
            <features>
                <feature>
                    <name>TestFeature</name>
                    <description>Test Description</description>
                </feature>
            </features>
            """;

        Feature created = new Feature();
        created.setId(1L);
        when(featureCreateService.create(any())).thenReturn(created);

        // Act
        int result = featureImportService.importXml(xml);

        // Assert
        assertTrue(result == 1, "Expected 1 feature counted as created");
    }

    @Test
    void testImportXml_FeatureCreationReturnsNull_NotCounted() throws Exception {
        // Arrange
        String xml = """
            <features>
                <feature>
                    <name>FailedFeature</name>
                </feature>
            </features>
            """;

        when(featureCreateService.create(any())).thenReturn(null);

        // Act
        int result = featureImportService.importXml(xml);

        // Assert
        assertTrue(result == 0, "Expected 0 features counted when creation returns null");
    }

    @Test
    void testImportXml_FeatureWithNullId_NotCounted() throws Exception {
        // Arrange
        String xml = """
            <features>
                <feature>
                    <name>NoIdFeature</name>
                </feature>
            </features>
            """;

        Feature created = new Feature();
        // ID is null
        when(featureCreateService.create(any())).thenReturn(created);

        // Act
        int result = featureImportService.importXml(xml);

        // Assert
        assertTrue(result == 0, "Expected 0 features counted when ID is null");
    }

    @Test
    void testImportXml_MultipleFeatures_SomeWithNullId() throws Exception {
        // Arrange
        String xml = """
            <features>
                <feature>
                    <name>Feature1</name>
                </feature>
                <feature>
                    <name>Feature2</name>
                </feature>
                <feature>
                    <name>Feature3</name>
                </feature>
            </features>
            """;

        Feature created1 = new Feature();
        created1.setId(1L);
        
        Feature created2 = new Feature();
        // ID is null
        
        Feature created3 = new Feature();
        created3.setId(3L);

        when(featureCreateService.create(any()))
            .thenReturn(created1)
            .thenReturn(created2)
            .thenReturn(created3);

        // Act
        int result = featureImportService.importXml(xml);

        // Assert
        assertTrue(result == 2, "Expected 2 features counted (excluding one with null ID)");
    }

    @Test
    void testImportXml_FallbackToPlainRoot_WhenBackupIsEmpty() throws Exception {
        // Arrange - XML with backup root but empty features, should fallback to plain features
        String xml = """
            <features>
                <feature>
                    <name>PlainFeature</name>
                    <description>Plain Description</description>
                </feature>
            </features>
            """;

        Feature created = new Feature();
        created.setId(1L);
        when(featureCreateService.create(any())).thenReturn(created);

        // Act
        int result = featureImportService.importXml(xml);

        // Assert
        assertTrue(result == 1, "Expected 1 feature imported from plain features root");
    }
}
