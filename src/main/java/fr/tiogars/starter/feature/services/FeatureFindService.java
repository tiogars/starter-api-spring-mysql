package fr.tiogars.starter.feature.services;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.common.services.AbstractFindService;
import fr.tiogars.starter.feature.entities.FeatureEntity;
import fr.tiogars.starter.feature.models.Feature;
import fr.tiogars.starter.feature.repositories.FeatureRepository;

@Service
public class FeatureFindService extends AbstractFindService<FeatureEntity, Long, Feature> {
    private final FeatureCreateService featureCreateService;

    public FeatureFindService(FeatureRepository featureRepository, FeatureCreateService featureCreateService) {
        super(featureRepository);
        this.featureCreateService = featureCreateService;
    }

    @Override
    protected Feature toModel(FeatureEntity entity) {
        return featureCreateService.toModel(entity);
    }
}
