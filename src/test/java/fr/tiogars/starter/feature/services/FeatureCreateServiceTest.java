package fr.tiogars.starter.feature.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tiogars.starter.app.entities.AppEntity;
import fr.tiogars.starter.app.services.AppService;
import fr.tiogars.starter.feature.entities.FeatureEntity;
import fr.tiogars.starter.feature.forms.FeatureCreateForm;
import fr.tiogars.starter.feature.models.Feature;
import fr.tiogars.starter.feature.repositories.FeatureRepository;
import fr.tiogars.starter.repository.entities.RepositoryEntity;
import fr.tiogars.starter.repository.services.RepositoryService;
import fr.tiogars.starter.tag.entities.TagEntity;
import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.tag.services.TagService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
class FeatureCreateServiceTest {

    @Mock private FeatureRepository featureRepository;
    @Mock private TagService tagService;
    @Mock private RepositoryService repositoryService;
    @Mock private AppService appService;
    @Mock private Validator validator;

    @InjectMocks private FeatureCreateService featureCreateService;

    private RepositoryEntity repo;
    private AppEntity app;

    @BeforeEach
    void setUp() {
        repo = new RepositoryEntity();
        repo.setId(10L);
        repo.setName("repo");
        app = new AppEntity();
        app.setId(20L);
        app.setName("app");

        lenient().when(validator.validate(any(Feature.class))).thenReturn(Set.of());
        lenient().when(repositoryService.findOrCreateByName("repo")).thenReturn(repo);
        lenient().when(appService.findOrCreateByName("app")).thenReturn(app);
    }

    @Test
    void toModelFromEntity_mapsAllFields() {
        FeatureEntity entity = new FeatureEntity();
        entity.setId(1L);
        entity.setName("feat");
        entity.setDescription("desc");

        TagEntity te = new TagEntity();
        te.setId(2L);
        te.setName("t1");
        te.setDescription("td");
        entity.setTags(Set.of(te));
        entity.setRepository(repo);
        entity.setApp(app);

        Feature model = featureCreateService.toModel(entity);
        assertEquals(1L, model.getId());
        assertEquals("feat", model.getName());
        assertEquals("desc", model.getDescription());
        assertEquals(1, model.getTags().size());
        assertEquals("t1", model.getTags().iterator().next().getName());
        assertSame(repo, model.getRepository());
        assertSame(app, model.getApp());
    }

    @Test
    void toEntityFromModel_mapsTagsAndRelations() {
        Feature model = new Feature();
        model.setName("feat");
        model.setDescription("desc");
        model.setTags(Set.of(new Tag(null, "t1")));
        model.setRepository(repo);
        model.setApp(app);

        when(tagService.findOrCreateTags(List.of("t1")))
            .thenReturn(List.of(new TagEntity("t1")));

        FeatureEntity entity = featureCreateService.toEntity(model);
        assertEquals("feat", entity.getName());
        assertEquals("desc", entity.getDescription());
        assertEquals(1, entity.getTags().size());
        assertSame(repo, entity.getRepository());
        assertSame(app, entity.getApp());
    }

    @Test
    void toModelFromForm_buildsModelWithServices() {
        FeatureCreateForm form = new FeatureCreateForm();
        form.setName("feat");
        form.setDescription("desc");
        form.setTagNames(List.of("t1", "t2"));
        form.setRepositoryName("repo");
        form.setAppName("app");

        Feature model = featureCreateService.toModel(form);
        assertEquals("feat", model.getName());
        assertEquals(2, model.getTags().size());
        assertSame(repo, model.getRepository());
        assertSame(app, model.getApp());
    }

    @Test
    void validate_throwsWhenViolations() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<Feature> violation = mock(ConstraintViolation.class);
        when(validator.validate(any(Feature.class))).thenReturn(Set.of(violation));
        Feature f = new Feature();
        assertThrows(ConstraintViolationException.class, () -> featureCreateService.validate(f));
    }
}
