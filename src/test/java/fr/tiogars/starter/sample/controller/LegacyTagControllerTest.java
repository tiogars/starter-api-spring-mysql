package fr.tiogars.starter.sample.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.tag.services.TagService;

/**
 * Unit tests for LegacyTagController.
 * Tests the legacy backward-compatible /sample-tag endpoints.
 */
@WebMvcTest(LegacyTagController.class)
class LegacyTagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    private Tag tag1;
    private Tag tag2;

    @BeforeEach
    void setUp() {
        tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Tag1");
        tag1.setDescription("Description 1");

        tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Tag2");
        tag2.setDescription("Description 2");
    }

    @Test
    void testGetAllTags_Success() throws Exception {
        // Arrange
        List<Tag> tags = Arrays.asList(tag1, tag2);
        when(tagService.findAll()).thenReturn(tags);

        // Act & Assert
        mockMvc.perform(get("/sample-tag")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Tag1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Tag2"));

        verify(tagService, times(1)).findAll();
    }

    @Test
    void testGetAllTags_EmptyList() throws Exception {
        // Arrange
        when(tagService.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/sample-tag")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(tagService, times(1)).findAll();
    }

    @Test
    void testGetTagById_Found() throws Exception {
        // Arrange
        when(tagService.findById(1L)).thenReturn(Optional.of(tag1));

        // Act & Assert
        mockMvc.perform(get("/sample-tag/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tag1"))
                .andExpect(jsonPath("$.description").value("Description 1"));

        verify(tagService, times(1)).findById(1L);
    }

    @Test
    void testGetTagById_NotFound() throws Exception {
        // Arrange
        when(tagService.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/sample-tag/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(tagService, times(1)).findById(999L);
    }

    @Test
    void testCreateTag_Success() throws Exception {
        // Arrange
        Tag newTag = new Tag();
        newTag.setName("NewTag");
        newTag.setDescription("New Description");

        Tag createdTag = new Tag();
        createdTag.setId(3L);
        createdTag.setName("NewTag");
        createdTag.setDescription("New Description");

        when(tagService.create(any(Tag.class))).thenReturn(createdTag);

        // Act & Assert
        mockMvc.perform(post("/sample-tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("NewTag"))
                .andExpect(jsonPath("$.description").value("New Description"));

        verify(tagService, times(1)).create(any(Tag.class));
    }

    @Test
    void testCreateTag_WithValidation() throws Exception {
        // Arrange
        Tag validTag = new Tag();
        validTag.setName("ValidTag");
        validTag.setDescription("Valid Description");

        Tag createdTag = new Tag();
        createdTag.setId(4L);
        createdTag.setName("ValidTag");
        createdTag.setDescription("Valid Description");

        when(tagService.create(any(Tag.class))).thenReturn(createdTag);

        // Act & Assert
        mockMvc.perform(post("/sample-tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validTag)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("ValidTag"));

        verify(tagService, times(1)).create(any(Tag.class));
    }

    @Test
    void testDeleteTag_Success() throws Exception {
        // Arrange
        doNothing().when(tagService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/sample-tag/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(tagService, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTag_DifferentId() throws Exception {
        // Arrange
        doNothing().when(tagService).deleteById(2L);

        // Act & Assert
        mockMvc.perform(delete("/sample-tag/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(tagService, times(1)).deleteById(2L);
    }
}
