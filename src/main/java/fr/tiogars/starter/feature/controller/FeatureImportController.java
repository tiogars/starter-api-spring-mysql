package fr.tiogars.starter.feature.controller;

import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.tiogars.starter.feature.services.FeatureImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/features")
@Tag(name = "feature", description = "CRUD operations for Features and XML import")
public class FeatureImportController {
    private final FeatureImportService featureImportService;

    public FeatureImportController(FeatureImportService featureImportService) {
        this.featureImportService = featureImportService;
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
