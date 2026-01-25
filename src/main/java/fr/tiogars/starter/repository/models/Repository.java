package fr.tiogars.starter.repository.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Repository {
    private Long id;

    @Size(min = 1, max = 255, message = "Repository name must not exceed 255 characters")
    @NotBlank(message = "Repository name cannot be blank")
    @NotNull(message = "Repository name cannot be null")
    private String name;

    @Size(max = 512, message = "URL must not exceed 512 characters")
    private String url;

    public Repository() {}
    public Repository(Long id, String name) { this.id = id; this.name = name; }
    public Repository(Long id, String name, String url) { 
        this.id = id; 
        this.name = name; 
        this.url = url; 
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
