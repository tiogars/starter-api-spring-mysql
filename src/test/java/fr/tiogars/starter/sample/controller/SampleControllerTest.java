package fr.tiogars.starter.sample.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tiogars.starter.sample.forms.SampleCreateForm;
import fr.tiogars.starter.sample.forms.SampleUpdateForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.services.SampleCreateService;
import fr.tiogars.starter.sample.services.SampleCrudService;
import fr.tiogars.starter.sample.services.SampleExportService;
import fr.tiogars.starter.sample.services.SampleImportService;
import fr.tiogars.starter.sample.services.SampleInitService;
import fr.tiogars.starter.sample.services.SampleSearchService;
import fr.tiogars.starter.sample.services.SampleUpdateService;

@WebMvcTest(SampleController.class)
class SampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SampleCreateService sampleCreateService;

    @MockBean
    private SampleCrudService sampleCrudService;

    @MockBean
    private SampleUpdateService sampleUpdateService;

    @MockBean
    private SampleImportService sampleImportService;

    @MockBean
    private SampleExportService sampleExportService;

    @MockBean
    private SampleInitService sampleInitService;

    @MockBean
    private SampleSearchService sampleSearchService;

    private Sample sample;
    private Date testDate;

    @BeforeEach
    void setUp() {
        testDate = new Date();
        sample = new Sample();
        sample.setId(1L);
        sample.setName("TestSample");
        sample.setDescription("Test Description");
        sample.setActive(true);
        sample.setCreatedAt(testDate);
        sample.setCreatedBy("testUser");
        sample.setUpdatedAt(testDate);
        sample.setUpdatedBy("testUser");
    }

    @Test
    void testCreateSample_ReturnsCreatedSample() throws Exception {
        // Arrange
        SampleCreateForm form = new SampleCreateForm();
        form.setName("NewSample");
        form.setDescription("New Description");
        form.setActive(true);

        when(sampleCreateService.create(any(SampleCreateForm.class))).thenReturn(sample);

        // Act & Assert
        mockMvc.perform(post("/sample")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestSample"));

        verify(sampleCreateService, times(1)).create(any(SampleCreateForm.class));
    }

    @Test
    void testGetSample_ReturnsSampleWhenExists() throws Exception {
        // Arrange
        when(sampleCrudService.findById(1L)).thenReturn(Optional.of(sample));

        // Act & Assert
        mockMvc.perform(get("/sample/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestSample"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(sampleCrudService, times(1)).findById(1L);
    }

    @Test
    void testGetSample_ReturnsNullWhenNotExists() throws Exception {
        // Arrange
        when(sampleCrudService.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/sample/999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(sampleCrudService, times(1)).findById(999L);
    }

    @Test
    void testGetAllSamples_ReturnsListOfSamples() throws Exception {
        // Arrange
        Sample sample2 = new Sample();
        sample2.setId(2L);
        sample2.setName("Sample2");
        
        List<Sample> samples = Arrays.asList(sample, sample2);
        when(sampleCrudService.findAll()).thenReturn(samples);

        // Act & Assert
        mockMvc.perform(get("/sample"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("TestSample"))
                .andExpect(jsonPath("$[1].name").value("Sample2"));

        verify(sampleCrudService, times(1)).findAll();
    }

    @Test
    void testGetAllSamples_ReturnsEmptyList() throws Exception {
        // Arrange
        when(sampleCrudService.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/sample"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sampleCrudService, times(1)).findAll();
    }

    @Test
    void testDeleteSample_CallsService() throws Exception {
        // Arrange
        doNothing().when(sampleCrudService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/sample/1"))
                .andExpect(status().isOk());

        verify(sampleCrudService, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateSample_ReturnsUpdatedSample() throws Exception {
        // Arrange
        SampleUpdateForm form = new SampleUpdateForm();
        form.setName("UpdatedSample");
        form.setDescription("Updated Description");
        form.setActive(false);

        Sample updatedSample = new Sample();
        updatedSample.setId(1L);
        updatedSample.setName("UpdatedSample");
        updatedSample.setDescription("Updated Description");
        updatedSample.setActive(false);

        when(sampleUpdateService.update(any(SampleUpdateForm.class))).thenReturn(updatedSample);

        // Act & Assert
        mockMvc.perform(put("/sample/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("UpdatedSample"))
                .andExpect(jsonPath("$.active").value(false));

        verify(sampleUpdateService, times(1)).update(any(SampleUpdateForm.class));
    }
}
