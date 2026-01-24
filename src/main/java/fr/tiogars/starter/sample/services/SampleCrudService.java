package fr.tiogars.starter.sample.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.sample.repositories.SampleRepository;

@Service
public class SampleCrudService {
    private final SampleRepository sampleRepository;

    public SampleCrudService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public List<Sample> findAll() {
        return sampleRepository.findAll().stream()
                .map(this::toModel)
                .toList();
    }

    public Optional<Sample> findById(Long id) {
        return sampleRepository.findById(id).map(this::toModel);
    }

    public Sample save(SampleEntity sample) {
        return toModel(sampleRepository.save(sample));
    }

    public void deleteById(Long id) {
        sampleRepository.deleteById(id);
    }

    private Sample toModel(SampleEntity entity) {
        Sample model = new Sample();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setActive(entity.isActive());
        model.setCreatedAt(entity.getCreatedAt());
        model.setCreatedBy(entity.getCreatedBy());
        model.setUpdatedAt(entity.getUpdatedAt());
        model.setUpdatedBy(entity.getUpdatedBy());
        
        // Convert tags
        if (entity.getTags() != null) {
            model.setTags(entity.getTags().stream()
                    .map(tag -> new Tag(tag.getId(), tag.getName(), tag.getDescription()))
                    .collect(Collectors.toSet()));
        }
        
        return model;
    }
}
