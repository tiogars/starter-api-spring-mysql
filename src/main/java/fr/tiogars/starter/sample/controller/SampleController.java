package fr.tiogars.starter.sample.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.starter.common.exception.ErrorResponse;
import fr.tiogars.starter.sample.forms.SampleCreateForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.services.SampleCreateService;
import fr.tiogars.starter.sample.services.SampleCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Sample
 */
@CrossOrigin
@RestController
@Tag(name = "sample", description = "CRUD operations for Sample entities")
public class SampleController {

    private SampleCreateService sampleCreateService;

    private SampleCrudService sampleCrudService;

    public SampleController(SampleCreateService sampleCreateService, SampleCrudService sampleCrudService) {
        this.sampleCreateService = sampleCreateService;
        this.sampleCrudService = sampleCrudService;
    }

    @PostMapping("sample")
    @Operation(summary = "Create a new sample", description = "Creates a new sample with validation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sample created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sample.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed - Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public Sample createSample(@RequestBody SampleCreateForm form) {
        
        return this.sampleCreateService.create(form);
    }

    @GetMapping("sample/{id}")
    public Sample getSample(@PathVariable Long id) {
        return this.sampleCrudService.findById(id)
                .orElse(null);
    }

    @GetMapping("sample")
    public List<Sample> getAllSamples() {
        return this.sampleCrudService.findAll();
    }

    @DeleteMapping("sample/{id}")
    public void deleteSample(@PathVariable Long id) {
        this.sampleCrudService.deleteById(id);
    }
}
