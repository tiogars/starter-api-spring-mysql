package fr.tiogars.starter.feature.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.starter.feature.forms.FeatureCreateForm;
import fr.tiogars.starter.feature.models.Feature;
import fr.tiogars.starter.feature.services.FeatureCreateService;
import fr.tiogars.starter.feature.services.FeatureCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/features")
@Tag(name = "feature", description = "CRUD operations for Features and XML import")
public class FeatureMutationController {
    private final FeatureCrudService featureCrudService;
    private final FeatureCreateService featureCreateService;

    public FeatureMutationController(FeatureCrudService featureCrudService, FeatureCreateService featureCreateService) {
        this.featureCrudService = featureCrudService;
        this.featureCreateService = featureCreateService;
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
}
