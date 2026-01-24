package fr.tiogars.starter.feature.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.feature.entities.FeatureEntity;
import fr.tiogars.starter.feature.models.Feature;
import fr.tiogars.starter.feature.repositories.FeatureRepository;

@Service
public class FeatureCrudService {
    private final FeatureRepository featureRepository;
    private final FeatureCreateService featureCreateService;

    public FeatureCrudService(FeatureRepository featureRepository, FeatureCreateService featureCreateService) {
        this.featureRepository = featureRepository;
        this.featureCreateService = featureCreateService;
    }

    public List<Feature> findAll() {
        return featureRepository.findAll().stream().map(featureCreateService::toModel).toList();
    }

    public Optional<Feature> findById(Long id) {
        return featureRepository.findById(id).map(featureCreateService::toModel);
    }

    public Feature create(Feature feature) {
        FeatureEntity entity = featureCreateService.toEntity(feature);
        return featureCreateService.toModel(featureRepository.save(entity));
    }

    public void deleteById(Long id) {
        featureRepository.deleteById(id);
    }
}
