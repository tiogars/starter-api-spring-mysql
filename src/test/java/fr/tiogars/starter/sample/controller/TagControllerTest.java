package fr.tiogars.starter.sample.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tiogars.starter.sample.models.SampleTag;
import fr.tiogars.starter.sample.services.SampleTagService;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SampleTagService sampleTagService;

    private SampleTag sampleTag;

    @BeforeEach
    void setUp() {
        sampleTag = new SampleTag(1L, "TestTag", "Test Description");
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        SampleTagService sampleTagService() { return mock(SampleTagService.class); }
    }

    @Test
    void testGetAllTags_ReturnsListOfTags() throws Exception {
        // Arrange
        SampleTag tag2 = new SampleTag(2L, "Tag2", "Description 2");
        List<SampleTag> tags = Arrays.asList(sampleTag, tag2);
        when(sampleTagService.findAll()).thenReturn(tags);

        // Act & Assert
        mockMvc.perform(get("/sample-tag"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("TestTag"))
                .andExpect(jsonPath("$[1].name").value("Tag2"));

        verify(sampleTagService, times(1)).findAll();
    }

    @Test
    void testGetAllTags_ReturnsEmptyList() throws Exception {
        // Arrange
        when(sampleTagService.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/sample-tag"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(sampleTagService, times(1)).findAll();
    }

    @Test
    void testGetTagById_ReturnsTagWhenExists() throws Exception {
        // Arrange
        when(sampleTagService.findById(1L)).thenReturn(Optional.of(sampleTag));

        // Act & Assert
        mockMvc.perform(get("/sample-tag/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestTag"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(sampleTagService, times(1)).findById(1L);
    }

    @Test
    void testGetTagById_ReturnsNotFoundWhenDoesNotExist() throws Exception {
        // Arrange
        when(sampleTagService.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/sample-tag/999"))
                .andExpect(status().isNotFound());

        verify(sampleTagService, times(1)).findById(999L);
    }

    @Test
    void testCreateTag_ReturnsCreatedTag() throws Exception {
        // Arrange
        SampleTag newTag = new SampleTag(null, "NewTag", "New Description");
        SampleTag createdTag = new SampleTag(3L, "NewTag", "New Description");
        when(sampleTagService.create(any(SampleTag.class))).thenReturn(createdTag);

        // Act & Assert
        mockMvc.perform(post("/sample-tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTag)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("NewTag"))
                .andExpect(jsonPath("$.description").value("New Description"));

        verify(sampleTagService, times(1)).create(any(SampleTag.class));
    }

    @Test
    void testDeleteTag_ReturnsNoContent() throws Exception {
        // Arrange
        doNothing().when(sampleTagService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/sample-tag/1"))
                .andExpect(status().isNoContent());

        verify(sampleTagService, times(1)).deleteById(1L);
    }
}
