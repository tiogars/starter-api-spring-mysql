package fr.tiogars.starter.feature.models;

import java.util.HashSet;
import java.util.Set;

import fr.tiogars.starter.app.entities.AppEntity;
import fr.tiogars.starter.repository.entities.RepositoryEntity;
import fr.tiogars.starter.tag.models.Tag;

public class Feature {
    private Long id;
    private String name;
    private String description;
    private Set<Tag> tags = new HashSet<>();
    private RepositoryEntity repository;
    private AppEntity app;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Tag> getTags() { return tags; }
    public void setTags(Set<Tag> tags) { this.tags = tags; }
    public RepositoryEntity getRepository() { return repository; }
    public void setRepository(RepositoryEntity repository) { this.repository = repository; }
    public AppEntity getApp() { return app; }
    public void setApp(AppEntity app) { this.app = app; }
}
