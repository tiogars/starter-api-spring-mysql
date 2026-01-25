package fr.tiogars.starter.sample.services;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.common.services.AbstractFindService;
import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.repositories.SampleRepository;
import fr.tiogars.starter.tag.models.Tag;

@Service
public class SampleFindService extends AbstractFindService<SampleEntity, Long, Sample> {

    public SampleFindService(SampleRepository sampleRepository) {
        super(sampleRepository);
    }

    @Override
    protected Sample toModel(SampleEntity entity) {
        Sample model = new Sample();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setActive(entity.isActive());
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());

        if (entity.getTags() != null) {
            model.setTags(entity.getTags().stream()
                    .map(tag -> new Tag(tag.getId(), tag.getName(), tag.getDescription()))
                    .collect(Collectors.toSet()));
        }

        return model;
    }
}
