package fr.tiogars.starter.service;

import org.springframework.stereotype.Service;

import fr.tiogars.architecture.create.services.AbstractCreateService;
import fr.tiogars.starter.entities.SampleEntity;
import fr.tiogars.starter.forms.SampleCreateForm;
import fr.tiogars.starter.models.Sample;
import fr.tiogars.starter.repository.SampleRepository;

@Service
public class SampleCreateService
        extends AbstractCreateService<SampleCreateForm, SampleEntity, Sample, SampleRepository> {

    public SampleCreateService(SampleRepository sampleRepository) {
        super(sampleRepository);
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
        
    }

}
