package fr.tiogars.starter.tag.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.tag.entities.TagEntity;
import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.tag.repositories.TagRepository;

@Service
public class TagService {
    private final TagRepository tagRepository;
    public TagService(TagRepository tagRepository) { this.tagRepository = tagRepository; }

    public List<Tag> findAll() {
        return tagRepository.findAll().stream().map(this::toModel).toList();
    }

    public Optional<Tag> findById(Long id) { return tagRepository.findById(id).map(this::toModel); }
    public Optional<Tag> findByName(String name) { return tagRepository.findByName(name).map(this::toModel);
    }

    public List<TagEntity> findOrCreateTags(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) { return List.of(); }
        List<TagEntity> existing = tagRepository.findByNameIn(tagNames);
        List<String> existingNames = existing.stream().map(TagEntity::getName).toList();
        List<TagEntity> created = tagNames.stream()
            .filter(n -> !existingNames.contains(n))
            .map(n -> tagRepository.save(new TagEntity(n)))
            .toList();
        existing.addAll(created);
        return existing;
    }

    public Tag create(Tag tag) {
        TagEntity entity = new TagEntity();
        entity.setName(tag.getName());
        entity.setDescription(tag.getDescription());
        return toModel(tagRepository.save(entity));
    }

    public void deleteById(Long id) { tagRepository.deleteById(id); }

    private Tag toModel(TagEntity entity) {
        return new Tag(entity.getId(), entity.getName(), entity.getDescription());
    }
}
