package fr.tiogars.starter.sample.services;

import java.util.Set;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import fr.tiogars.architecture.create.services.AbstractCreateService;
import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.forms.SampleCreateForm;
import fr.tiogars.starter.sample.models.Sample;
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

    public SampleCreateService(SampleRepository sampleRepository, Validator validator) {
        super(sampleRepository);
        this.validator = validator;
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
