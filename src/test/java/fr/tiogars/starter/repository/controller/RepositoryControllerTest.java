package fr.tiogars.starter.repository.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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

import fr.tiogars.starter.repository.models.Repository;
import fr.tiogars.starter.repository.services.RepositoryService;

import static org.mockito.Mockito.mock;

@WebMvcTest(RepositoryController.class)
class RepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RepositoryService repositoryService;

    private Repository repository;

    @BeforeEach
    void setUp() {
        reset(repositoryService);
        repository = new Repository();
        repository.setId(1L);
        repository.setName("TestRepository");
        repository.setUrl("https://github.com/test/repo");
    }

    @Test
    void shouldCreateRepositorySuccessfully() throws Exception {
        // Given
        Repository createRepository = new Repository();
        createRepository.setName("NewRepository");
        createRepository.setUrl("https://github.com/new/repo");

        when(repositoryService.create(any(Repository.class))).thenReturn(repository);

        // When & Then
        mockMvc.perform(post("/repositories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRepository)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestRepository"));

        verify(repositoryService, times(1)).create(any(Repository.class));
    }

    @Test
    void shouldReturnRepositoryWhenIdExists() throws Exception {
        // Given
        when(repositoryService.findById(1L)).thenReturn(Optional.of(repository));

        // When & Then
        mockMvc.perform(get("/repositories/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestRepository"))
                .andExpect(jsonPath("$.url").value("https://github.com/test/repo"));

        verify(repositoryService, times(1)).findById(1L);
    }

    @Test
    void shouldReturn404WhenRepositoryNotFound() throws Exception {
        // Given
        when(repositoryService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/repositories/999"))
                .andExpect(status().isNotFound());

        verify(repositoryService, times(1)).findById(999L);
    }

    @Test
    void shouldReturnAllRepositories() throws Exception {
        // Given
        Repository repository2 = new Repository();
        repository2.setId(2L);
        repository2.setName("Repository2");
        
        List<Repository> repositories = Arrays.asList(repository, repository2);
        when(repositoryService.findAll()).thenReturn(repositories);

        // When & Then
        mockMvc.perform(get("/repositories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("TestRepository"))
                .andExpect(jsonPath("$[1].name").value("Repository2"));

        verify(repositoryService, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        // Given
        when(repositoryService.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/repositories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(repositoryService, times(1)).findAll();
    }

    @Test
    void shouldDeleteRepositorySuccessfully() throws Exception {
        // Given
        doNothing().when(repositoryService).deleteById(1L);

        // When & Then
        mockMvc.perform(delete("/repositories/1"))
                .andExpect(status().isNoContent());

        verify(repositoryService, times(1)).deleteById(1L);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public RepositoryService repositoryService() {
            return mock(RepositoryService.class);
        }
    }
}
