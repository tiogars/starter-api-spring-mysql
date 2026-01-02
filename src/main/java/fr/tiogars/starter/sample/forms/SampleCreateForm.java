package fr.tiogars.starter.sample.forms;

import fr.tiogars.architecture.create.forms.AbstractCreateForm;

public class SampleCreateForm extends AbstractCreateForm {
    private String name;

    /**
     * Description of the sample
     */
    private String description;

    /**
     * Active status
     */
    private boolean active;

    public SampleCreateForm() {
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
}
