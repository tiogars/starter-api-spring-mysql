package fr.tiogars.starter.sample.forms;

import java.util.List;

/**
 * Form for importing multiple samples at once.
 * Contains a list of sample creation forms to process.
 * 
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
public class SampleImportForm {
    
    /**
     * List of samples to import
     */
    private List<SampleCreateForm> samples;

    public SampleImportForm() {
    }

    public List<SampleCreateForm> getSamples() {
        return samples;
    }

    public void setSamples(List<SampleCreateForm> samples) {
        this.samples = samples;
    }
}
