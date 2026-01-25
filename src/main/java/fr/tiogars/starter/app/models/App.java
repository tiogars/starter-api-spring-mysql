package fr.tiogars.starter.app.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class App {
    private Long id;

    @Size(min = 1, max = 255, message = "Application name must not exceed 255 characters")
    @NotBlank(message = "Application name cannot be blank")
    @NotNull(message = "Application name cannot be null")
    private String name;

    @Size(max = 64, message = "Version must not exceed 64 characters")
    private String version;

    public App() {}
    public App(Long id, String name) { this.id = id; this.name = name; }
    public App(Long id, String name, String version) { 
        this.id = id; 
        this.name = name; 
        this.version = version; 
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
}
