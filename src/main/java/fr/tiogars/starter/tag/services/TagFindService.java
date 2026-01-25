package fr.tiogars.starter.tag.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.tiogars.starter.common.services.AbstractFindService;
import fr.tiogars.starter.tag.entities.TagEntity;
import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.tag.repositories.TagRepository;

@Service
public class TagFindService extends AbstractFindService<TagEntity, Long, Tag> {

    private final TagRepository tagRepository;

    public TagFindService(TagRepository tagRepository) {
        super(tagRepository);
        this.tagRepository = tagRepository;
    }

    @Override
    protected Tag toModel(TagEntity entity) {
        return new Tag(entity.getId(), entity.getName(), entity.getDescription());
    }

    public Optional<Tag> findByName(String name) {
        return tagRepository.findByName(name).map(this::toModel);
    }
}
