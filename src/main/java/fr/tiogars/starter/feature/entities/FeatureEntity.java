package fr.tiogars.starter.feature.entities;

import java.util.HashSet;
import java.util.Set;

import fr.tiogars.architecture.create.entities.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feature")
public class FeatureEntity extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255, unique = true)
    private String name;

    @Column(length = 1024)
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "feature_tag",
        joinColumns = @JoinColumn(name = "feature_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<fr.tiogars.starter.tag.entities.TagEntity> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "repository_id")
    private fr.tiogars.starter.repository.entities.RepositoryEntity repository;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private fr.tiogars.starter.app.entities.AppEntity app;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Set<fr.tiogars.starter.tag.entities.TagEntity> getTags() { return tags; }
    public void setTags(Set<fr.tiogars.starter.tag.entities.TagEntity> tags) { this.tags = tags; }

    public fr.tiogars.starter.repository.entities.RepositoryEntity getRepository() { return repository; }
    public void setRepository(fr.tiogars.starter.repository.entities.RepositoryEntity repository) { this.repository = repository; }

    public fr.tiogars.starter.app.entities.AppEntity getApp() { return app; }
    public void setApp(fr.tiogars.starter.app.entities.AppEntity app) { this.app = app; }
}
