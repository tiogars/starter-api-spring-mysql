package fr.tiogars.starter.feature.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.feature.entities.FeatureEntity;
import fr.tiogars.starter.feature.models.Feature;
import fr.tiogars.starter.feature.repositories.FeatureRepository;

@ExtendWith(MockitoExtension.class)
class FeatureCrudServiceTest {

    @Mock private FeatureRepository featureRepository;
    @Mock private FeatureCreateService featureCreateService;
    @InjectMocks private FeatureCrudService featureCrudService;

    private FeatureEntity entity;
    private Feature model;

    @BeforeEach
    void setUp() {
        entity = new FeatureEntity();
        entity.setId(1L);
        entity.setName("f1");
        model = new Feature();
        model.setId(1L);
        model.setName("f1");
        lenient().when(featureCreateService.toModel(entity)).thenReturn(model);
        lenient().when(featureCreateService.toEntity(any(Feature.class))).thenReturn(entity);
    }

    @Test
    void findAll_mapsEntitiesToModels() {
        when(featureRepository.findAll()).thenReturn(List.of(entity));
        List<Feature> all = featureCrudService.findAll();
        assertEquals(1, all.size());
        assertEquals("f1", all.get(0).getName());
    }

    @Test
    void findById_returnsEmptyWhenNotFound() {
        when(featureRepository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(featureCrudService.findById(99L).isEmpty());
    }

    @Test
    void findById_mapsEntityToModel() {
        when(featureRepository.findById(1L)).thenReturn(Optional.of(entity));
        Optional<Feature> res = featureCrudService.findById(1L);
        assertTrue(res.isPresent());
        assertEquals("f1", res.get().getName());
    }

    @Test
    void create_savesEntityAndReturnsModel() {
        when(featureRepository.save(entity)).thenReturn(entity);
        Feature saved = featureCrudService.create(model);
        assertEquals("f1", saved.getName());
        verify(featureRepository).save(entity);
    }

    @Test
    void deleteById_callsRepository() {
        featureCrudService.deleteById(5L);
        verify(featureRepository).deleteById(5L);
    }
}
