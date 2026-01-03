package fr.tiogars.starter.sample.forms;

import fr.tiogars.starter.sample.models.SampleSearchRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Form for exporting samples in various formats.
 * Allows filtering and export configuration.
 * 
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
public class SampleExportForm {
    
    /**
     * Export format: xlsx, csv, xml, or json
     */
    @NotBlank(message = "Format cannot be blank")
    @Pattern(regexp = "^(xlsx|csv|xml|json)$", message = "Format must be one of: xlsx, csv, xml, json")
    private String format;
    
    /**
     * Whether to compress the exported file as ZIP
     */
    private boolean zip;
    
    /**
     * Optional search criteria to filter samples for export
     */
    @Valid
    private SampleSearchRequest searchRequest;

    public SampleExportForm() {
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isZip() {
        return zip;
    }

    public void setZip(boolean zip) {
        this.zip = zip;
    }

    public SampleSearchRequest getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(SampleSearchRequest searchRequest) {
        this.searchRequest = searchRequest;
    }
}
