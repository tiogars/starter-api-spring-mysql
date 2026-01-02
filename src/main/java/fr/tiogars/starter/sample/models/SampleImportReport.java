package fr.tiogars.starter.sample.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Report for bulk sample import operation.
 * Provides detailed statistics and individual item results for React Material UI consumption.
 * 
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
public class SampleImportReport {
    
    /**
     * Total number of samples provided for import
     */
    private int totalProvided;
    
    /**
     * Number of samples successfully created
     */
    private int totalCreated;
    
    /**
     * Number of samples skipped due to duplicate names
     */
    private int totalDuplicates;
    
    /**
     * Number of samples that failed validation or other errors
     */
    private int totalErrors;
    
    /**
     * Overall alert level for the import operation: "success", "info", "warning", "error"
     */
    private String alertLevel;
    
    /**
     * Overall message summarizing the import operation
     */
    private String message;
    
    /**
     * Detailed results for each sample
     */
    private List<SampleImportReportItem> items;

    public SampleImportReport() {
        this.items = new ArrayList<>();
    }

    public int getTotalProvided() {
        return totalProvided;
    }

    public void setTotalProvided(int totalProvided) {
        this.totalProvided = totalProvided;
    }

    public int getTotalCreated() {
        return totalCreated;
    }

    public void setTotalCreated(int totalCreated) {
        this.totalCreated = totalCreated;
    }

    public int getTotalDuplicates() {
        return totalDuplicates;
    }

    public void setTotalDuplicates(int totalDuplicates) {
        this.totalDuplicates = totalDuplicates;
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public void setTotalErrors(int totalErrors) {
        this.totalErrors = totalErrors;
    }

    public String getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(String alertLevel) {
        this.alertLevel = alertLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SampleImportReportItem> getItems() {
        return items;
    }

    public void setItems(List<SampleImportReportItem> items) {
        this.items = items;
    }
    
    public void addItem(SampleImportReportItem item) {
        this.items.add(item);
    }
}
