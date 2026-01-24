package fr.tiogars.starter.feature.forms;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * Represents the full backup root (<backup>) structure containing features among other sections.
 * We only map the features list and ignore other sections (tags, repositories, apps).
 */
@JacksonXmlRootElement(localName = "backup")
public class BackupXmlImportForm {
    @JacksonXmlElementWrapper(localName = "features")
    @JacksonXmlProperty(localName = "feature")
    private List<FeatureXmlImportForm.FeatureXmlItem> features;

    public List<FeatureXmlImportForm.FeatureXmlItem> getFeatures() { return features; }
    public void setFeatures(List<FeatureXmlImportForm.FeatureXmlItem> features) { this.features = features; }
}
