package fr.tiogars.starter.route.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.route.entities.RouteEntity;
import fr.tiogars.starter.route.forms.RouteCreateForm;
import fr.tiogars.starter.route.forms.RouteUpdateForm;
import fr.tiogars.starter.route.models.Route;
import fr.tiogars.starter.route.repositories.RouteRepository;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteService routeService;

    private RouteEntity routeEntity;

    @BeforeEach
    void setUp() {
        routeEntity = new RouteEntity();
        routeEntity.setId(1L);
        routeEntity.setName("Test Route");
        routeEntity.setPath("/test/path");
    }

    @Test
    void testFindAll_ReturnsListOfRoutes() {
        // Arrange
        RouteEntity entity2 = new RouteEntity();
        entity2.setId(2L);
        entity2.setName("Route 2");
        entity2.setPath("/route2");

        when(routeRepository.findAll()).thenReturn(Arrays.asList(routeEntity, entity2));

        // Act
        List<Route> result = routeService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Route", result.get(0).getName());
        assertEquals("/test/path", result.get(0).getPath());
        assertEquals("Route 2", result.get(1).getName());
        verify(routeRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ReturnsEmptyList() {
        // Arrange
        when(routeRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Route> result = routeService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(routeRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ReturnsRouteWhenExists() {
        // Arrange
        when(routeRepository.findById(1L)).thenReturn(Optional.of(routeEntity));

        // Act
        Route result = routeService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Route", result.getName());
        assertEquals("/test/path", result.getPath());
        verify(routeRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_ReturnsNullWhenNotExists() {
        // Arrange
        when(routeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Route result = routeService.findById(999L);

        // Assert
        assertNull(result);
        verify(routeRepository, times(1)).findById(999L);
    }

    @Test
    void testSave_ReturnsSavedRoute() {
        // Arrange
        RouteCreateForm form = new RouteCreateForm();
        form.setName("New Route");
        form.setPath("/new/path");

        when(routeRepository.save(any(RouteEntity.class))).thenReturn(routeEntity);

        // Act
        Route result = routeService.save(form);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Route", result.getName());
        assertEquals("/test/path", result.getPath());
        verify(routeRepository, times(1)).save(any(RouteEntity.class));
    }

    @Test
    void testUpdate_ReturnsUpdatedRoute() {
        // Arrange
        RouteUpdateForm form = new RouteUpdateForm();
        form.setId(1L);
        form.setName("Updated Route");
        form.setPath("/updated/path");

        when(routeRepository.save(any(RouteEntity.class))).thenReturn(routeEntity);

        // Act
        Route result = routeService.update(form);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Route", result.getName());
        verify(routeRepository, times(1)).save(any(RouteEntity.class));
    }

    @Test
    void testDeleteById_CallsRepository() {
        // Arrange
        Long idToDelete = 1L;
        doNothing().when(routeRepository).deleteById(idToDelete);

        // Act
        routeService.deleteById(idToDelete);

        // Assert
        verify(routeRepository, times(1)).deleteById(idToDelete);
    }
}
