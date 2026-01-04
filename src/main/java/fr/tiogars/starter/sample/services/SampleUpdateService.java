package fr.tiogars.starter.sample.services;

import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.forms.SampleUpdateForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.models.SampleTag;
import fr.tiogars.starter.sample.repositories.SampleRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@Service
@Validated
public class SampleUpdateService {

    private final Logger logger = Logger.getLogger(SampleUpdateService.class.getName());
    private final SampleRepository sampleRepository;
    private final Validator validator;
    private final SampleTagService sampleTagService;

    public SampleUpdateService(SampleRepository sampleRepository, Validator validator, SampleTagService sampleTagService) {
        this.sampleRepository = sampleRepository;
        this.validator = validator;
        this.sampleTagService = sampleTagService;
    }

    public Sample update(SampleUpdateForm form) {
        // Fetch existing entity to preserve creation fields
        SampleEntity existingEntity = sampleRepository.findById(form.getId())
                .orElseThrow(() -> new RuntimeException("Sample not found with id: " + form.getId()));
        
        Sample sample = toModel(form, existingEntity);
        validate(sample);
        SampleEntity entity = toEntity(sample, existingEntity);
        SampleEntity savedEntity = sampleRepository.save(entity);
        return toModel(savedEntity);
    }

    private SampleEntity toEntity(Sample sample, SampleEntity existingEntity) {
        existingEntity.setName(sample.getName());
        existingEntity.setDescription(sample.getDescription());
        existingEntity.setActive(sample.isActive());
        existingEntity.setUpdatedAt(sample.getUpdatedAt());
        existingEntity.setUpdatedBy(sample.getUpdatedBy());
        
        // Handle tags
        if (sample.getTags() != null && !sample.getTags().isEmpty()) {
            var tagNames = sample.getTags().stream()
                    .map(SampleTag::getName)
                    .collect(Collectors.toList());
            var tagEntities = sampleTagService.findOrCreateTags(tagNames);
            existingEntity.setTags(tagEntities.stream().collect(Collectors.toSet()));
        } else {
            existingEntity.setTags(Set.of());
        }
        
        return existingEntity;
    }

    private Sample toModel(SampleEntity entity) {
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
                    .map(tag -> new SampleTag(tag.getId(), tag.getName(), tag.getDescription()))
                    .collect(Collectors.toSet()));
        }
        
        return sample;
    }

    private Sample toModel(SampleUpdateForm form, SampleEntity existingEntity) {
        Sample sample = new Sample();
        sample.setId(form.getId());
        sample.setName(form.getName());
        sample.setDescription(form.getDescription());
        sample.setActive(form.isActive());
        sample.setUpdatedAt(new java.util.Date());
        sample.setUpdatedBy("system");
        // Preserve creation fields from existing entity
        sample.setCreatedAt(existingEntity.getCreatedAt());
        sample.setCreatedBy(existingEntity.getCreatedBy());
        
        // Handle tags from form
        if (form.getTagNames() != null && !form.getTagNames().isEmpty()) {
            sample.setTags(form.getTagNames().stream()
                    .map(name -> new SampleTag(null, name))
                    .collect(Collectors.toSet()));
        }
        
        return sample;
    }

    private void validate(Sample sample) {
        logger.info("Validating sample update: " + sample.getName());
        Set<ConstraintViolation<Sample>> violations = validator.validate(sample);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
