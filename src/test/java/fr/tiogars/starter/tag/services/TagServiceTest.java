package fr.tiogars.starter.tag.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.tag.entities.TagEntity;
import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.tag.repositories.TagRepository;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock private TagRepository tagRepository;
    @InjectMocks private TagService tagService;

    @Test
    void findOrCreateTags_handlesNullAndEmpty() {
        assertTrue(tagService.findOrCreateTags(null).isEmpty());
        assertTrue(tagService.findOrCreateTags(List.of()).isEmpty());
    }

    @Test
    void findOrCreateTags_createsMissingAndReturnsAll() {
        java.util.List<TagEntity> existing = new java.util.ArrayList<>();
        existing.add(new TagEntity("a"));
        when(tagRepository.findByNameIn(List.of("a","b")))
            .thenReturn(existing);
        when(tagRepository.save(any(TagEntity.class)))
            .thenAnswer(inv -> { TagEntity e = inv.getArgument(0); e.setId(2L); return e;});

        List<TagEntity> res = tagService.findOrCreateTags(List.of("a","b"));
        assertEquals(2, res.size());
        assertTrue(res.stream().anyMatch(t -> "a".equals(t.getName())));
        assertTrue(res.stream().anyMatch(t -> "b".equals(t.getName())));
    }

    @Test
    void findById_and_findByName_mapToModel() {
        TagEntity e = new TagEntity("x"); e.setId(1L); e.setDescription("d");
        when(tagRepository.findById(1L)).thenReturn(Optional.of(e));
        when(tagRepository.findByName("x")).thenReturn(Optional.of(e));

        assertTrue(tagService.findById(1L).isPresent());
        assertEquals("x", tagService.findByName("x").get().getName());
    }

    @Test
    void create_savesEntity() {
        TagEntity saved = new TagEntity(); saved.setId(3L); saved.setName("n");
        when(tagRepository.save(any(TagEntity.class))).thenReturn(saved);
        Tag out = tagService.create(new Tag(null, "n", "d"));
        assertEquals(3L, out.getId());
        assertEquals("n", out.getName());
    }
}
