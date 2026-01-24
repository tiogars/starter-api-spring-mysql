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
}
