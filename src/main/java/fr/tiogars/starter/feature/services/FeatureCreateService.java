package fr.tiogars.starter.feature.services;

import java.util.stream.Collectors;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fr.tiogars.architecture.create.services.AbstractCreateService;
import fr.tiogars.starter.app.services.AppService;
import fr.tiogars.starter.feature.entities.FeatureEntity;
import fr.tiogars.starter.feature.forms.FeatureCreateForm;
import fr.tiogars.starter.feature.models.Feature;
import fr.tiogars.starter.feature.repositories.FeatureRepository;
import fr.tiogars.starter.repository.services.RepositoryService;
import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.tag.services.TagService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
@Validated
public class FeatureCreateService extends AbstractCreateService<FeatureCreateForm, FeatureEntity, Feature, FeatureRepository> {
    private final TagService tagService;
    private final RepositoryService repositoryService;
    private final AppService appService;
    private final Validator validator;

    public FeatureCreateService(FeatureRepository featureRepository, TagService tagService, RepositoryService repositoryService, AppService appService, Validator validator) {
        super(featureRepository);
        this.tagService = tagService;
        this.repositoryService = repositoryService;
        this.appService = appService;
        this.validator = validator;
    }

    @Override
    public FeatureEntity toEntity(Feature feature) {
        FeatureEntity entity = new FeatureEntity();
        entity.setName(feature.getName());
        entity.setDescription(feature.getDescription());
        if (feature.getTags() != null && !feature.getTags().isEmpty()) {
            var tagNames = feature.getTags().stream().map(Tag::getName).toList();
            var tagEntities = tagService.findOrCreateTags(tagNames);
            entity.setTags(tagEntities.stream().collect(Collectors.toSet()));
        }
        entity.setRepository(feature.getRepository());
        entity.setApp(feature.getApp());
        return entity;
    }

    @Override
    public Feature toModel(FeatureEntity entity) {
        Feature feature = new Feature();
        feature.setId(entity.getId());
        feature.setName(entity.getName());
        feature.setDescription(entity.getDescription());
        if (entity.getTags() != null) {
            feature.setTags(entity.getTags().stream()
                .map(t -> new Tag(t.getId(), t.getName(), t.getDescription()))
                .collect(Collectors.toSet()));
        }
        feature.setRepository(entity.getRepository());
        feature.setApp(entity.getApp());
        return feature;
    }

    @Override
    public Feature toModel(FeatureCreateForm form) {
        Feature feature = new Feature();
        feature.setName(form.getName());
        feature.setDescription(form.getDescription());
        if (form.getTagNames() != null && !form.getTagNames().isEmpty()) {
            feature.setTags(form.getTagNames().stream().map(n -> new Tag(null, n)).collect(Collectors.toSet()));
        }
        if (form.getRepositoryName() != null) {
            feature.setRepository(repositoryService.findOrCreateByName(form.getRepositoryName()));
        }
        if (form.getAppName() != null) {
            feature.setApp(appService.findOrCreateByName(form.getAppName()));
        }
        return feature;
    }

    @Override
    public void validate(Feature feature) {
        Set<ConstraintViolation<Feature>> violations = validator.validate(feature);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
