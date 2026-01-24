package fr.tiogars.starter.sample.services;

import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fr.tiogars.architecture.create.services.AbstractCreateService;
import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.forms.SampleCreateForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.sample.repositories.SampleRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
@Validated
public class SampleCreateService
        extends AbstractCreateService<SampleCreateForm, SampleEntity, Sample, SampleRepository> {

    private final Logger logger = Logger.getLogger(SampleCreateService.class.getName());
    private final Validator validator;
    private final fr.tiogars.starter.tag.services.TagService tagService;

    public SampleCreateService(SampleRepository sampleRepository, Validator validator, fr.tiogars.starter.tag.services.TagService tagService) {
        super(sampleRepository);
        this.validator = validator;
        this.tagService = tagService;
    }

    @Override
    public SampleEntity toEntity(Sample sample) {
        SampleEntity entity = new SampleEntity();
        entity.setName(sample.getName());
        entity.setDescription(sample.getDescription());
        entity.setActive(sample.isActive());
        entity.setCreatedAt(sample.getCreatedAt());
        entity.setCreatedBy(sample.getCreatedBy());
        entity.setUpdatedAt(sample.getUpdatedAt());
        entity.setUpdatedBy(sample.getUpdatedBy());
        
        // Handle tags
        if (sample.getTags() != null && !sample.getTags().isEmpty()) {
                var tagNames = sample.getTags().stream()
                    .map(Tag::getName)
                    .toList();
            var tagEntities = tagService.findOrCreateTags(tagNames);
            entity.setTags(tagEntities.stream().collect(Collectors.toSet()));
        }
        
        return entity;
    }

    @Override
    public Sample toModel(SampleEntity entity) {
        Sample sample = new Sample();
        sample.setId(entity.getId());
        sample.setName(entity.getName());
        sample.setDescription(entity.getDescription());
        sample.setActive(entity.isActive());
        sample.setCreatedAt(entity.getCreatedAt());
        sample.setCreatedBy(entity.getCreatedBy());
        sample.setUpdatedAt(entity.getUpdatedAt());
        sample.setUpdatedBy(entity.getUpdatedBy());
        
        // Convert tags
        if (entity.getTags() != null) {
            sample.setTags(entity.getTags().stream()
                .map(tag -> new Tag(tag.getId(), tag.getName(), tag.getDescription()))
                .collect(Collectors.toSet()));
        }
        
        return sample;
    }

    @Override
    public Sample toModel(SampleCreateForm form) {
        Sample sample = new Sample();
        sample.setName(form.getName());
        sample.setDescription(form.getDescription());
        sample.setActive(form.isActive());
        sample.setCreatedAt(new java.util.Date());
        sample.setCreatedBy("system");
        sample.setUpdatedAt(new java.util.Date());
        sample.setUpdatedBy("system");
        
        // Handle tags from form
        if (form.getTagNames() != null && !form.getTagNames().isEmpty()) {
            sample.setTags(form.getTagNames().stream()
                .map(name -> new Tag(null, name))
                .collect(Collectors.toSet()));
        }
        
        return sample;
    }

    @Override
    public void validate(Sample sample) {
        Set<ConstraintViolation<Sample>> violations = validator.validate(sample);
    if (!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
    }
    logger.info("Validating sample: " + sample.getName());
    }

}
