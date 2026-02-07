package fr.tiogars.starter.sample.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.tiogars.starter.sample.entities.SampleEntity;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.repositories.SampleRepository;
import fr.tiogars.starter.tag.entities.TagEntity;

class SampleFindServiceTest {

    @Test
    void toModel_nullTags_keepsDefaultTags() {
        SampleFindService service = new SampleFindService(mock(SampleRepository.class));
        SampleEntity entity = new SampleEntity();
        entity.setId(1L);
        entity.setName("Sample");
        entity.setTags(null);

        Sample result = service.toModel(entity);

        assertNotNull(result.getTags());
        assertTrue(result.getTags().isEmpty());
    }

    @Test
    void toModel_withTags_mapsTags() {
        SampleFindService service = new SampleFindService(mock(SampleRepository.class));
        SampleEntity entity = new SampleEntity();
        entity.setId(2L);
        entity.setName("Sample 2");

        TagEntity tagEntity = new TagEntity("Tag", "Desc");
        tagEntity.setId(10L);
        Set<TagEntity> tags = new HashSet<>();
        tags.add(tagEntity);
        entity.setTags(tags);

        Sample result = service.toModel(entity);

        assertNotNull(result.getTags());
        assertEquals(1, result.getTags().size());
        assertEquals("Tag", result.getTags().iterator().next().getName());
    }
}
