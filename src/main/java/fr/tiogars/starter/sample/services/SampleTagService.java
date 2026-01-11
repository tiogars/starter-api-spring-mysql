package fr.tiogars.starter.sample.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.sample.entities.SampleTagEntity;
import fr.tiogars.starter.sample.models.SampleTag;
import fr.tiogars.starter.sample.repositories.SampleTagRepository;

/**
 * Service for managing sample tags.
 */
@Service
public class SampleTagService {
    private final SampleTagRepository sampleTagRepository;

    public SampleTagService(SampleTagRepository sampleTagRepository) {
        this.sampleTagRepository = sampleTagRepository;
    }

    /**
     * Get all tags
     */
    public List<SampleTag> findAll() {
        return sampleTagRepository.findAll().stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    /**
     * Get tag by id
     */
    public Optional<SampleTag> findById(Long id) {
        return sampleTagRepository.findById(id).map(this::toModel);
    }

    /**
     * Get tag by name
     */
    public Optional<SampleTag> findByName(String name) {
        return sampleTagRepository.findByName(name).map(this::toModel);
    }

    /**
     * Find or create tags by name list
     */
    public List<SampleTagEntity> findOrCreateTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return List.of();
        }
        
        List<SampleTagEntity> existingTags = sampleTagRepository.findByNameIn(tagNames)
            .stream()
            .collect(Collectors.toList());
        List<String> existingNames = existingTags.stream()
                .map(SampleTagEntity::getName)
                .collect(Collectors.toList());
        
        List<String> newTagNames = tagNames.stream()
                .filter(name -> !existingNames.contains(name))
                .collect(Collectors.toList());
        
        List<SampleTagEntity> newTags = newTagNames.stream()
                .map(name -> sampleTagRepository.save(new SampleTagEntity(name)))
                .collect(Collectors.toList());
        
        existingTags.addAll(newTags);
        return existingTags;
    }

    /**
     * Create a new tag
     */
    public SampleTag create(SampleTag tag) {
        SampleTagEntity entity = new SampleTagEntity();
        entity.setName(tag.getName());
        entity.setDescription(tag.getDescription());
        return toModel(sampleTagRepository.save(entity));
    }

    /**
     * Delete tag by id
     */
    public void deleteById(Long id) {
        sampleTagRepository.deleteById(id);
    }

    /**
     * Convert entity to model
     */
    private SampleTag toModel(SampleTagEntity entity) {
        return new SampleTag(entity.getId(), entity.getName(), entity.getDescription());
    }
}
