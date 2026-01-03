package fr.tiogars.starter.sample.forms;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Form for initializing multiple samples with mock data.
 * 
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
public class SampleInitForm {
    
    /**
     * Number of samples to create
     */
    @NotNull(message = "Number of samples cannot be null")
    @Min(value = 1, message = "Number of samples must be at least 1")
    @Max(value = 100, message = "Number of samples cannot exceed 100")
    private Integer numberOfSamples;

    public SampleInitForm() {
    }

    public Integer getNumberOfSamples() {
        return numberOfSamples;
    }

    public void setNumberOfSamples(Integer numberOfSamples) {
        this.numberOfSamples = numberOfSamples;
    }
}
