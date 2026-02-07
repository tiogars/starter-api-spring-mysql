package fr.tiogars.starter.app.controller;

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

import fr.tiogars.starter.app.models.App;
import fr.tiogars.starter.app.services.AppService;

import static org.mockito.Mockito.mock;

@WebMvcTest({AppQueryController.class, AppMutationController.class})
class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AppService appService;

    private App app;

    @BeforeEach
    void setUp() {
        reset(appService);
        app = new App();
        app.setId(1L);
        app.setName("TestApp");
        app.setVersion("1.0.0");
    }

    @Test
    void shouldCreateAppSuccessfully() throws Exception {
        // Given
        App createApp = new App();
        createApp.setName("NewApp");
        createApp.setVersion("2.0.0");

        when(appService.create(any(App.class))).thenReturn(app);

        // When & Then
        mockMvc.perform(post("/apps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createApp)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestApp"));

        verify(appService, times(1)).create(any(App.class));
    }

    @Test
    void shouldReturnAppWhenIdExists() throws Exception {
        // Given
        when(appService.findById(1L)).thenReturn(Optional.of(app));

        // When & Then
        mockMvc.perform(get("/apps/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("TestApp"))
                .andExpect(jsonPath("$.version").value("1.0.0"));

        verify(appService, times(1)).findById(1L);
    }

    @Test
    void shouldReturn404WhenAppNotFound() throws Exception {
        // Given
        when(appService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/apps/999"))
                .andExpect(status().isNotFound());

        verify(appService, times(1)).findById(999L);
    }

    @Test
    void shouldReturnAllApps() throws Exception {
        // Given
        App app2 = new App();
        app2.setId(2L);
        app2.setName("App2");
        
        List<App> apps = Arrays.asList(app, app2);
        when(appService.findAll()).thenReturn(apps);

        // When & Then
        mockMvc.perform(get("/apps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("TestApp"))
                .andExpect(jsonPath("$[1].name").value("App2"));

        verify(appService, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        // Given
        when(appService.findAll()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/apps"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(appService, times(1)).findAll();
    }

    @Test
    void shouldDeleteAppSuccessfully() throws Exception {
        // Given
        doNothing().when(appService).deleteById(1L);

        // When & Then
        mockMvc.perform(delete("/apps/1"))
                .andExpect(status().isNoContent());

        verify(appService, times(1)).deleteById(1L);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public AppService appService() {
            return mock(AppService.class);
        }
    }
}
