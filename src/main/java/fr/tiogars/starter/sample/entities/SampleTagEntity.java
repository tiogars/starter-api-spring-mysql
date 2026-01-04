package fr.tiogars.starter.sample.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Sample tag entity for managing sample tags.
 * 
 * This entity represents a tag that can be associated with samples
 * for categorization and filtering purposes.
 * 
 * @Entity JPA entity mapped to database table
 * @author Generated
 * @version 1.0
 */
@Entity
@Table(name = "sample_tag")
public class SampleTagEntity {
    
    /**
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Tag name
     */
    @Column(nullable = false, length = 100, unique = true)
    private String name;

    /**
     * Tag description
     */
    @Column(nullable = true, length = 255)
    private String description;

    public SampleTagEntity() {
    }

    public SampleTagEntity(String name) {
        this.name = name;
    }

    public SampleTagEntity(String name, String description) {
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
