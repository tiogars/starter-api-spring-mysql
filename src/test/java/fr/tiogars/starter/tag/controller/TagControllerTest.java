package fr.tiogars.starter.tag.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tiogars.starter.common.services.dto.FindResponse;
import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.tag.services.TagFindService;
import fr.tiogars.starter.tag.services.TagService;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TagService tagService;

    @Autowired
    private TagFindService tagFindService;

    private Tag tag;

    @BeforeEach
    void setUp() {
        reset(tagService);
        reset(tagFindService);
        tag = new Tag(1L, "TestTag", "Test Description");
    }

    @Test
    void shouldReturnAllTags() throws Exception {
        // Arrange
        Tag tag2 = new Tag(2L, "Tag2", "Description 2");
        FindResponse<Tag> tags = new FindResponse<>(Arrays.asList(tag, tag2));
        when(tagFindService.findAll()).thenReturn(tags);

        // Act & Assert
        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("TestTag"))
                .andExpect(jsonPath("$.data[1].name").value("Tag2"));

        verify(tagFindService, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoTags() throws Exception {
        // Arrange
        FindResponse<Tag> tags = new FindResponse<>(List.of());
        when(tagFindService.findAll()).thenReturn(tags);

        // Act & Assert
        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(0))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));

        verify(tagFindService, times(1)).findAll();
    }

    @Test
    void shouldReturnTagByIdWhenExists() throws Exception {
        // Arrange
            Tag response = new Tag();
            response.setId(1L);
            response.setName("TestTag");
            response.setDescription("Test Description");
            when(tagFindService.findById(1L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/tags/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("TestTag"));

        verify(tagFindService, times(1)).findById(1L);
    }

    @Test
    void shouldReturn404WhenTagNotFound() throws Exception {
        // Arrange
            Tag response = new Tag();
            when(tagFindService.findById(999L)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/tags/999"))
                .andExpect(status().isNotFound());

        verify(tagFindService, times(1)).findById(999L);
    }

    @Test
    void shouldCreateTagSuccessfully() throws Exception {
        // Arrange
        Tag newTag = new Tag(null, "NewTag", "New Description");
        Tag createdTag = new Tag(3L, "NewTag", "New Description");

        when(tagService.create(any(Tag.class))).thenReturn(createdTag);

        // Act & Assert
        mockMvc.perform(post("/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTag)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("NewTag"));

        verify(tagService, times(1)).create(any(Tag.class));
    }

    @Test
    void shouldDeleteTagSuccessfully() throws Exception {
        // Arrange
        doNothing().when(tagService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/tags/1"))
                .andExpect(status().isNoContent());

        verify(tagService, times(1)).deleteById(1L);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        TagService tagService() { return mock(TagService.class); }

        @Bean
        TagFindService tagFindService() { return mock(TagFindService.class); }
    }
}
