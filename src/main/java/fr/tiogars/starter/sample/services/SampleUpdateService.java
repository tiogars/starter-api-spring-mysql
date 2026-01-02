package fr.tiogars.starter.sample.services;

import java.util.Set;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.forms.SampleUpdateForm;
import fr.tiogars.starter.sample.models.Sample;
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

    public SampleUpdateService(SampleRepository sampleRepository, Validator validator) {
        this.sampleRepository = sampleRepository;
        this.validator = validator;
    }

    public Sample update(SampleUpdateForm form) {
        // Fetch existing entity to preserve creation fields
        SampleEntity existingEntity = sampleRepository.findById(form.getId())
                .orElseThrow(() -> new RuntimeException("Sample not found with id: " + form.getId()));
        
        Sample sample = toModel(form, existingEntity);
        validate(sample);
        SampleEntity entity = toEntity(sample);
        SampleEntity savedEntity = sampleRepository.save(entity);
        return toModel(savedEntity);
    }

    private SampleEntity toEntity(Sample sample) {
        SampleEntity entity = new SampleEntity();
        entity.setId(sample.getId());
        entity.setName(sample.getName());
        entity.setDescription(sample.getDescription());
        entity.setActive(sample.isActive());
        entity.setCreatedAt(sample.getCreatedAt());
        entity.setCreatedBy(sample.getCreatedBy());
        entity.setUpdatedAt(sample.getUpdatedAt());
        entity.setUpdatedBy(sample.getUpdatedBy());
        return entity;
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
        return sample;
    }

    private void validate(Sample sample) {
        Set<ConstraintViolation<Sample>> violations = validator.validate(sample);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        logger.info("Validating sample update: " + sample.getName());
    }
}
