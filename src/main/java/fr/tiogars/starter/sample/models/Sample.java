package fr.tiogars.starter.sample.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents a sample model with basic CRUD tracking capabilities.
 * This class serves as a data model for managing sample records with 
 * audit trail information including creation and modification timestamps
 * and user tracking.
 * 
 * <p>The Sample class includes the following key features:
 * <ul>
 *   <li>Unique identifier for database persistence</li>
 *   <li>Basic metadata (name, description)</li>
 *   <li>Active status flag for soft deletion/activation</li>
 *   <li>Complete audit trail with creation and update tracking</li>
 * </ul>
 * 
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
public class Sample {
    private Long id;

    /**
     * Name of the sample
     */
    @Size(min=1, max = 10, message = "Name must not exceed 10 characters")
    @NotBlank(message = "Name cannot be blank")
    @NotEmpty(message = "Name cannot be empty")
    @NotNull(message = "Name cannot be null")
    private String name;

    /**
     * Description of the sample
     */
    private String description;

    /**
     * Active status
     */
    private boolean active;

    /**
     * Created at timestamp
     */
    private Date createdAt;

    /**
     * Created by user
     */
    private String createdBy;

    /**
     * Updated at timestamp
     */
    private Date updatedAt;

    /**
     * Updated by user
     */
    private String updatedBy;

    /**
     * Tags associated with this sample
     */
    private Set<SampleTag> tags = new HashSet<>();

    public Sample() {
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Set<SampleTag> getTags() {
        return tags;
    }

    public void setTags(Set<SampleTag> tags) {
        this.tags = tags;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
