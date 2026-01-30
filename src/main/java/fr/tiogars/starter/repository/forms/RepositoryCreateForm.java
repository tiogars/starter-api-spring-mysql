package fr.tiogars.starter.repository.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RepositoryCreateForm {
    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 1024)
    private String description;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
