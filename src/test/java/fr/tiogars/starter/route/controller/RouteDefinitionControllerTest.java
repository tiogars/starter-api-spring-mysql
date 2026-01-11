package fr.tiogars.starter.route.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tiogars.starter.route.forms.RouteCreateForm;
import fr.tiogars.starter.route.forms.RouteUpdateForm;
import fr.tiogars.starter.route.models.Route;
import fr.tiogars.starter.route.services.RouteService;

@WebMvcTest(RouteDefinitionController.class)
class RouteDefinitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RouteService routeService;

    private Route route;

    @BeforeEach
    void setUp() {
        route = new Route();
        route.setId(1L);
        route.setName("Test Route");
        route.setPath("/test/path");
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        RouteService routeService() {
            return mock(RouteService.class);
        }
    }

    @Test
    void testGetAllRoute_ReturnsListOfRoutes() throws Exception {
        // Arrange
        Route route2 = new Route();
        route2.setId(2L);
        route2.setName("Route 2");
        route2.setPath("/route2");

        List<Route> routes = Arrays.asList(route, route2);
        when(routeService.findAll()).thenReturn(routes);

        // Act & Assert
        mockMvc.perform(get("/routes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test Route"))
                .andExpect(jsonPath("$[1].name").value("Route 2"));

        verify(routeService, times(1)).findAll();
    }

    @Test
    void testGetAllRoute_ReturnsEmptyList() throws Exception {
        // Arrange
        when(routeService.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/routes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(routeService, times(1)).findAll();
    }

    @Test
    void testCreateRoute_ReturnsCreatedRoute() throws Exception {
        // Arrange
        RouteCreateForm form = new RouteCreateForm();
        form.setName("New Route");
        form.setPath("/new/path");

        when(routeService.save(any(RouteCreateForm.class))).thenReturn(route);

        // Act & Assert
        mockMvc.perform(post("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Route"))
                .andExpect(jsonPath("$.path").value("/test/path"));

        verify(routeService, times(1)).save(any(RouteCreateForm.class));
    }

    @Test
    void testGetRouteById_ReturnsRoute() throws Exception {
        // Arrange
        when(routeService.findById(1L)).thenReturn(route);

        // Act & Assert
        mockMvc.perform(get("/routes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Route"))
                .andExpect(jsonPath("$.path").value("/test/path"));

        verify(routeService, times(1)).findById(1L);
    }

    @Test
    void testGetRouteById_ReturnsNull() throws Exception {
        // Arrange
        when(routeService.findById(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/routes/999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(routeService, times(1)).findById(999L);
    }

    @Test
    void testUpdateRoute_ReturnsUpdatedRoute() throws Exception {
        // Arrange
        RouteUpdateForm form = new RouteUpdateForm();
        form.setId(1L);
        form.setName("Updated Route");
        form.setPath("/updated/path");

        Route updatedRoute = new Route();
        updatedRoute.setId(1L);
        updatedRoute.setName("Updated Route");
        updatedRoute.setPath("/updated/path");

        when(routeService.update(any(RouteUpdateForm.class))).thenReturn(updatedRoute);

        // Act & Assert
        mockMvc.perform(put("/routes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Route"))
                .andExpect(jsonPath("$.path").value("/updated/path"));

        verify(routeService, times(1)).update(any(RouteUpdateForm.class));
    }

    @Test
    void testDeleteRoute_CallsService() throws Exception {
        // Arrange
        doNothing().when(routeService).deleteById(1L);

        // Act & Assert
        mockMvc.perform(delete("/routes/1"))
                .andExpect(status().isOk());

        verify(routeService, times(1)).deleteById(1L);
    }
}
