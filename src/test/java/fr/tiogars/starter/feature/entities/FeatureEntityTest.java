package fr.tiogars.starter.feature.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.tiogars.starter.app.entities.AppEntity;
import fr.tiogars.starter.repository.entities.RepositoryEntity;
import fr.tiogars.starter.tag.entities.TagEntity;

class FeatureEntityTest {
    @Test
    void gettersAndSettersWork() {
        FeatureEntity e = new FeatureEntity();
        e.setId(1L);
        e.setName("n");
        e.setDescription("d");
        TagEntity t = new TagEntity("t");
        e.setTags(Set.of(t));
        RepositoryEntity r = new RepositoryEntity(); r.setName("r");
        AppEntity a = new AppEntity(); a.setName("a");
        e.setRepository(r);
        e.setApp(a);

        assertEquals(1L, e.getId());
        assertEquals("n", e.getName());
        assertEquals("d", e.getDescription());
        assertEquals(1, e.getTags().size());
        assertSame(r, e.getRepository());
        assertSame(a, e.getApp());
    }
}
