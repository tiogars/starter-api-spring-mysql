package fr.tiogars.starter.feature.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.starter.common.services.dto.FindResponse;
import fr.tiogars.starter.feature.models.Feature;
import fr.tiogars.starter.feature.services.FeatureFindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/features")
@Tag(name = "feature", description = "CRUD operations for Features and XML import")
public class FeatureQueryController {
    private final FeatureFindService featureFindService;

    public FeatureQueryController(FeatureFindService featureFindService) {
        this.featureFindService = featureFindService;
    }

    @GetMapping
    @Operation(summary = "Get all features")
    public ResponseEntity<FindResponse<Feature>> getAll() {
        return ResponseEntity.ok(featureFindService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feature by id")
    public ResponseEntity<Feature> getById(@PathVariable Long id) {
        Feature response = featureFindService.findById(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}
