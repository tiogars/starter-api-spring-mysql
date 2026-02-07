package fr.tiogars.starter.feature.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tiogars.starter.common.services.dto.FindResponse;
import fr.tiogars.starter.feature.forms.FeatureCreateForm;
import fr.tiogars.starter.feature.models.Feature;
import fr.tiogars.starter.feature.services.FeatureCreateService;
import fr.tiogars.starter.feature.services.FeatureCrudService;
import fr.tiogars.starter.feature.services.FeatureFindService;
import fr.tiogars.starter.feature.services.FeatureImportService;

@WebMvcTest({FeatureQueryController.class, FeatureMutationController.class, FeatureImportController.class})
class FeatureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private FeatureCrudService featureCrudService;

    @Autowired
    private FeatureImportService featureImportService;

    @Autowired
    private FeatureCreateService featureCreateService;

    @Autowired
    private FeatureFindService featureFindService;

    private Feature feature;
    private FeatureCreateForm createForm;

    @BeforeEach
    void setUp() {
        reset(featureCrudService, featureImportService, featureCreateService, featureFindService);
        feature = new Feature();
        feature.setId(1L);
        feature.setName("Test Feature");
        feature.setDescription("Test Description");

        createForm = new FeatureCreateForm();
        createForm.setName("New Feature");
        createForm.setDescription("New Description");
    }

    @Test
    void shouldReturnAllFeatures() throws Exception {
        // Arrange
        Feature feature2 = new Feature();
        feature2.setId(2L);
        feature2.setName("Feature 2");
        FindResponse<Feature> features = new FindResponse<>(Arrays.asList(feature, feature2));
        when(featureFindService.findAll()).thenReturn(features);

        // Act & Assert
        mockMvc.perform(get("/features"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Test Feature"))
                .andExpect(jsonPath("$.data[1].name").value("Feature 2"));

        verify(featureFindService, times(1)).findAll();
    }

    @Test
    void shouldReturnFeatureByIdWhenExists() throws Exception {
        // Arrange
        Feature response = new Feature();
        response.setId(1L);
        response.setName("Test Feature");
        response.setDescription("Test Description");
        when(featureFindService.findById(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/features/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Feature"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(featureFindService, times(1)).findById(1L);
    }

    @Test
    void shouldReturn404WhenFeatureNotFound() throws Exception {
        // Arrange
        Feature response = null;
        when(featureFindService.findById(999L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/features/999"))
                .andExpect(status().isNotFound());

        verify(featureFindService, times(1)).findById(999L);
    }

    @Test
    void shouldCreateFeatureSuccessfully() throws Exception {
        // Arrange
        when(featureCreateService.create(any(FeatureCreateForm.class))).thenReturn(feature);

        // Act & Assert
        mockMvc.perform(post("/features")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createForm)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Feature"));

        verify(featureCreateService, times(1)).create(any(FeatureCreateForm.class));
    }

    @Test
    void shouldDeleteFeatureSuccessfully() throws Exception {
        // Arrange
        doNothing().when(featureCrudService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/features/1"))
                .andExpect(status().isNoContent());

        verify(featureCrudService, times(1)).deleteById(1L);
    }

    @Test
    void shouldImportFeaturesFromXmlSuccessfully() throws Exception {
        // Arrange
        String xmlContent = """
            <features>
                <feature>
                    <name>Feature 1</name>
                    <description>Description 1</description>
                </feature>
            </features>
            """;
        when(featureImportService.importXml(anyString())).thenReturn(1);

        // Act & Assert
        mockMvc.perform(post("/features/import")
                .contentType(MediaType.APPLICATION_XML)
                .content(xmlContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Imported 1 features"));

        verify(featureImportService, times(1)).importXml(anyString());
    }

    @Test
    void shouldImportFeaturesFromXmlFileUpload() throws Exception {
        // Arrange
        String xmlContent = """
                <features>
                    <feature>
                        <name>Feature 1</name>
                        <description>Desc 1</description>
                    </feature>
                </features>
                """;
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "features.xml",
                MediaType.APPLICATION_XML_VALUE,
                xmlContent.getBytes()
        );
        when(featureImportService.importXml(anyString())).thenReturn(1);

        // Act & Assert
        mockMvc.perform(multipart("/features/import").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Imported 1 features"));

        verify(featureImportService, times(1)).importXml(anyString());
    }

    @Test
    void shouldImportMultipleFeaturesFromXml() throws Exception {
        // Arrange
        String xmlContent = """
            <features>
                <feature>
                    <name>Feature 1</name>
                </feature>
                <feature>
                    <name>Feature 2</name>
                </feature>
            </features>
            """;
        when(featureImportService.importXml(anyString())).thenReturn(2);

        // Act & Assert
        mockMvc.perform(post("/features/import")
                .contentType(MediaType.APPLICATION_XML)
                .content(xmlContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Imported 2 features"));

        verify(featureImportService, times(1)).importXml(anyString());
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        FeatureCrudService featureCrudService() { return mock(FeatureCrudService.class); }

        @Bean
        FeatureImportService featureImportService() { return mock(FeatureImportService.class); }

        @Bean
        FeatureCreateService featureCreateService() { return mock(FeatureCreateService.class); }

        @Bean
        FeatureFindService featureFindService() { return mock(FeatureFindService.class); }
    }
}
