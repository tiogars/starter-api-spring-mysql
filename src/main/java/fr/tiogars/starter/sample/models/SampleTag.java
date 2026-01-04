package fr.tiogars.starter.sample.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents a sample tag model for managing tags.
 * 
 * @author Generated
 * @version 1.0
 */
public class SampleTag {
    private Long id;

    /**
     * Tag name
     */
    @Size(min = 1, max = 100, message = "Tag name must not exceed 100 characters")
    @NotBlank(message = "Tag name cannot be blank")
    @NotNull(message = "Tag name cannot be null")
    private String name;

    /**
     * Tag description
     */
    @Size(max = 255, message = "Tag description must not exceed 255 characters")
    private String description;

    public SampleTag() {
    }

    public SampleTag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SampleTag(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
}
