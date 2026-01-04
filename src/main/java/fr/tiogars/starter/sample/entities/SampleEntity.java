package fr.tiogars.starter.sample.entities;

import java.util.Date;
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
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;

/**
 * Sample entity representing a basic data model with audit fields.
 * 
 * This entity extends AbstractEntity and provides a standard structure for
 * sample data with common audit tracking capabilities including creation
 * and update timestamps along with user information.
 * 
 * Features:
 * - Auto-generated primary key using IDENTITY strategy
 * - Basic entity properties (name, description, active status)
 * - Complete audit trail with creation and update tracking
 * - User tracking for creation and modification operations
 * 
 * @Entity JPA entity mapped to database table
 * @author Generated
 * @version 1.0
 */
@Entity
@Table(name = "sample")
public class SampleEntity extends AbstractEntity {

    /**
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the sample
     */
    @Column(nullable = false, length = 255)
    private String name;

    /**
     * Description of the sample
     */
    @Column(nullable = true, length = 255)
    private String description;

    /**
     * Active status
     */
    @Column(nullable = false)
    private boolean active;

    /**
     * Created at timestamp
     */
    @Column(nullable = false)
    private Date createdAt;

    /**
     * Created by user
     */
    @Column(nullable = false, length = 255)
    private String createdBy;

    /**
     * Updated at timestamp
     */
    @Column(nullable = false)
    private Date updatedAt;

    /**
     * Updated by user
     */
    @Column(nullable = false, length = 255)
    private String updatedBy;

    /**
     * Tags associated with this sample
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "sample_sample_tag", joinColumns = @JoinColumn(name = "sample_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<SampleTagEntity> tags = new HashSet<>();

    public SampleEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
        
    public Set<SampleTagEntity> getTags() {
        return tags;
    }

    public void setTags(Set<SampleTagEntity> tags) {
        this.tags = tags;
    }

    public void addTag(SampleTagEntity tag) {
        this.tags.add(tag);
    }

    public void removeTag(SampleTagEntity tag) {
        this.tags.remove(tag);
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

}
