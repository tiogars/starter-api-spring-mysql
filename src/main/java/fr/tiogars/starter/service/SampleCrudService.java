package fr.tiogars.starter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.entities.SampleEntity;
import fr.tiogars.starter.models.Sample;
import fr.tiogars.starter.repository.SampleRepository;

@Service
public class SampleCrudService {
    private final SampleRepository sampleRepository;

    public SampleCrudService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public List<Sample> findAll() {
        return sampleRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
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
        return model;
    }
}
