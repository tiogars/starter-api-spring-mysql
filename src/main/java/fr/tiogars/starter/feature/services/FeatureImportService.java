package fr.tiogars.starter.feature.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import fr.tiogars.starter.feature.forms.BackupXmlImportForm;
import fr.tiogars.starter.feature.forms.FeatureCreateForm;
import fr.tiogars.starter.feature.forms.FeatureXmlImportForm;
import fr.tiogars.starter.feature.forms.FeatureXmlImportForm.FeatureXmlItem;
import fr.tiogars.starter.feature.models.Feature;

@Service
public class FeatureImportService {
    private final Logger logger = Logger.getLogger(FeatureImportService.class.getName());
    private final FeatureCreateService featureCreateService;

    public FeatureImportService(FeatureCreateService featureCreateService) {
        this.featureCreateService = featureCreateService;
    }

    public int importXml(String xmlContent) throws JsonProcessingException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<FeatureXmlItem> items = null;
        // First, try the backup root (<backup><features>...)
        BackupXmlImportForm backup = xmlMapper.readValue(xmlContent, BackupXmlImportForm.class);
        if (backup != null && backup.getFeatures() != null && !backup.getFeatures().isEmpty()) {
            items = backup.getFeatures();
        } else {
            // Fallback to plain <features> root
            FeatureXmlImportForm root = xmlMapper.readValue(xmlContent, FeatureXmlImportForm.class);
            if (root != null) {
                items = root.getFeatures();
            }
        }

        if (items == null || items.isEmpty()) {
            return 0;
        }

        int created = 0;
        for (FeatureXmlItem item : items) {
            FeatureCreateForm form = new FeatureCreateForm();
            form.setName(item.getName());
            form.setDescription(item.getDescription());
            form.setTagNames(item.getTag());
            form.setRepositoryName(item.getRepository());
            form.setAppName(item.getApp());
            Feature feature = featureCreateService.create(form);
            if (feature != null && feature.getId() != null) {
                created++;
            }
        }
        logger.info(String.format("Imported features from XML: %d", created));
        return created;
    }
}
