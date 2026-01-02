package fr.tiogars.starter.sample.models;

/**
 * Represents the result of importing a single sample.
 * Contains information about whether the sample was created or skipped due to duplicate name.
 * 
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
public class SampleImportReportItem {
    
    /**
     * The name of the sample that was attempted to import
     */
    private String name;
    
    /**
     * Whether the sample was successfully created
     */
    private boolean created;
    
    /**
     * Message explaining the result (e.g., "Sample created successfully" or "Sample with this name already exists")
     */
    private String message;
    
    /**
     * Alert level for Material UI: "success", "info", "warning", "error"
     */
    private String alertLevel;

    public SampleImportReportItem() {
    }

    public SampleImportReportItem(String name, boolean created, String message, String alertLevel) {
        this.name = name;
        this.created = created;
        this.message = message;
        this.alertLevel = alertLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }
}
