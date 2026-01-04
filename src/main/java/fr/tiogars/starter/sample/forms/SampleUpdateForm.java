package fr.tiogars.starter.sample.forms;

import java.util.HashSet;
import java.util.Set;

public class SampleUpdateForm {

    private Long id;

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
     * Tags associated with this sample
     */
    private Set<String> tagNames = new HashSet<>();

    public SampleUpdateForm() {
    }

    public SampleUpdateForm(Long id, String name, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
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

    public Set<String> getTagNames() {
        return tagNames;
    }

    public void setTagNames(Set<String> tagNames) {
        this.tagNames = tagNames;
    }
}
