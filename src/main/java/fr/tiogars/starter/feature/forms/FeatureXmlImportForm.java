package fr.tiogars.starter.feature.forms;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "features")
public class FeatureXmlImportForm {
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "feature")
    private List<FeatureXmlItem> features;

    public List<FeatureXmlItem> getFeatures() { return features; }
    public void setFeatures(List<FeatureXmlItem> features) { this.features = features; }

    public static class FeatureXmlItem {
        @JsonAlias({"name", "title"})
        @JacksonXmlProperty(localName = "name")
        private String name;
        @JacksonXmlProperty(localName = "description")
        private String description;
        @JacksonXmlElementWrapper(localName = "tags")
        @JacksonXmlProperty(localName = "tag")
        private List<String> tag;
        @JacksonXmlProperty(localName = "repository")
        private String repository;
        @JacksonXmlProperty(localName = "app")
        private String app;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<String> getTag() { return tag; }
        public void setTag(List<String> tag) { this.tag = tag; }
        public String getRepository() { return repository; }
        public void setRepository(String repository) { this.repository = repository; }
        public String getApp() { return app; }
        public void setApp(String app) { this.app = app; }
    }
}
