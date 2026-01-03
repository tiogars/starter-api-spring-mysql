package fr.tiogars.starter.sample.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import fr.tiogars.starter.common.exception.ErrorResponse;
import fr.tiogars.starter.sample.forms.SampleCreateForm;
import fr.tiogars.starter.sample.forms.SampleImportForm;
import fr.tiogars.starter.sample.forms.SampleInitForm;
import fr.tiogars.starter.sample.forms.SampleUpdateForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.models.SampleImportReport;
import fr.tiogars.starter.sample.models.SampleSearchRequest;
import fr.tiogars.starter.sample.models.SampleSearchResponse;
import fr.tiogars.starter.sample.services.SampleCreateService;
import fr.tiogars.starter.sample.services.SampleCrudService;
import fr.tiogars.starter.sample.services.SampleImportService;
import fr.tiogars.starter.sample.services.SampleInitService;
import fr.tiogars.starter.sample.services.SampleSearchService;
import fr.tiogars.starter.sample.services.SampleUpdateService;
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

    private SampleUpdateService sampleUpdateService;

    private SampleImportService sampleImportService;

    private SampleInitService sampleInitService;

    private SampleSearchService sampleSearchService;

    public SampleController(SampleCreateService sampleCreateService, SampleCrudService sampleCrudService,
            SampleUpdateService sampleUpdateService, SampleImportService sampleImportService,
            SampleInitService sampleInitService, SampleSearchService sampleSearchService) {
        this.sampleCreateService = sampleCreateService;
        this.sampleCrudService = sampleCrudService;
        this.sampleUpdateService = sampleUpdateService;
        this.sampleImportService = sampleImportService;
        this.sampleInitService = sampleInitService;
        this.sampleSearchService = sampleSearchService;
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

    @PutMapping("sample/{id}")
    @Operation(summary = "Update an existing sample", description = "Updates a sample with validation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sample updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Sample.class))),
        @ApiResponse(responseCode = "400", description = "Validation failed - Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Sample not found")
    })
    public Sample updateSample(@PathVariable Long id, @RequestBody SampleUpdateForm form) {
        form.setId(id);
        return this.sampleUpdateService.update(form);
    }

    @PostMapping("sample/import")
    @Operation(summary = "Import multiple samples", description = "Imports multiple samples in bulk, checking for duplicates by name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Import completed with detailed report",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SampleImportReport.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SampleImportReport importSamples(@RequestBody SampleImportForm form) {
        return this.sampleImportService.importSamples(form);
    }

    @PostMapping("sample/init")
    @Operation(summary = "Initialize samples with mock data", description = "Creates a specified number of samples with randomly generated mock data for testing and development purposes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Samples initialized successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<Sample> initSamples(@Valid @RequestBody SampleInitForm form) {
        return this.sampleInitService.initSamples(form);
    }

    @PostMapping("sample/search")
    @Operation(summary = "Search samples with pagination and filtering", 
               description = "Search samples with support for pagination, sorting, and filtering on all Sample fields. Compatible with MUI X DataGrid v7.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search completed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SampleSearchResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid search criteria",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public SampleSearchResponse searchSamples(@RequestBody SampleSearchRequest request) {
        return this.sampleSearchService.search(request);
    }
}
