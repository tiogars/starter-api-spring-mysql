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

import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.tag.services.TagService;
import fr.tiogars.starter.tag.services.TagFindService;

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
        List<Tag> tags = Arrays.asList(tag, tag2);
        when(tagFindService.findAll()).thenReturn(tags);

        // Act & Assert
        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("TestTag"))
                .andExpect(jsonPath("$[1].name").value("Tag2"));

        verify(tagFindService, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoTags() throws Exception {
        // Arrange
        when(tagFindService.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(tagFindService, times(1)).findAll();
    }

    @Test
    void shouldReturnTagByIdWhenExists() throws Exception {
        // Arrange
        when(tagFindService.findById(1L)).thenReturn(Optional.of(tag));

        // Act & Assert
        mockMvc.perform(get("/tags/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestTag"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(tagFindService, times(1)).findById(1L);
    }

    @Test
    void shouldReturn404WhenTagNotFound() throws Exception {
        // Arrange
        when(tagFindService.findById(999L)).thenReturn(Optional.empty());

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
