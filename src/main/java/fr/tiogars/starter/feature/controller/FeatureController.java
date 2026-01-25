package fr.tiogars.starter.feature.controller;

import java.util.List;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import fr.tiogars.starter.feature.forms.FeatureCreateForm;
import fr.tiogars.starter.feature.models.Feature;
import fr.tiogars.starter.feature.services.FeatureCrudService;
import fr.tiogars.starter.feature.services.FeatureFindService;
import fr.tiogars.starter.feature.services.FeatureCreateService;
import fr.tiogars.starter.feature.services.FeatureImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/features")
@Tag(name = "feature", description = "CRUD operations for Features and XML import")
public class FeatureController {
    private final FeatureCrudService featureCrudService;
    private final FeatureFindService featureFindService;
    private final FeatureImportService featureImportService;
    private final FeatureCreateService featureCreateService;

    public FeatureController(FeatureCrudService featureCrudService, FeatureImportService featureImportService, FeatureCreateService featureCreateService, FeatureFindService featureFindService) {
        this.featureCrudService = featureCrudService;
        this.featureImportService = featureImportService;
        this.featureCreateService = featureCreateService;
        this.featureFindService = featureFindService;
    }

    @GetMapping
    @Operation(summary = "Get all features")
    public ResponseEntity<List<Feature>> getAll() { return ResponseEntity.ok(featureFindService.findAll()); }

    @GetMapping("/{id}")
    @Operation(summary = "Get feature by id")
    public ResponseEntity<Feature> getById(@PathVariable Long id) {
        return featureFindService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create feature")
    public ResponseEntity<Feature> create(@RequestBody FeatureCreateForm form) {
        Feature feature = featureCreateService.create(form);
        return ResponseEntity.ok(feature);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feature")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        featureCrudService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/import", consumes = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Import features from XML")
    public ResponseEntity<String> importXml(@RequestBody String xmlContent) throws Exception {
        int created = featureImportService.importXml(xmlContent);
        return ResponseEntity.ok("Imported " + created + " features");
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import features from uploaded XML file")
    public ResponseEntity<String> importXmlFile(@RequestPart("file") MultipartFile file) throws Exception {
        String xmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        int created = featureImportService.importXml(xmlContent);
        return ResponseEntity.ok("Imported " + created + " features");
    }
}
