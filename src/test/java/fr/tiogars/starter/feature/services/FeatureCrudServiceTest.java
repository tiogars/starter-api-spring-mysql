package fr.tiogars.starter.feature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
