package fr.tiogars.starter.feature.forms;

import java.util.List;

import fr.tiogars.architecture.create.forms.AbstractCreateForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FeatureCreateForm extends AbstractCreateForm {
    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 1024)
    private String description;

    private List<String> tagNames;
    private String repositoryName;
    private String appName;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getTagNames() { return tagNames; }
    public void setTagNames(List<String> tagNames) { this.tagNames = tagNames; }
    public String getRepositoryName() { return repositoryName; }
    public void setRepositoryName(String repositoryName) { this.repositoryName = repositoryName; }
    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
}
