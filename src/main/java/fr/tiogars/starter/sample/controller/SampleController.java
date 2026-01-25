package fr.tiogars.starter.sample.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import fr.tiogars.starter.sample.forms.SampleExportForm;
import fr.tiogars.starter.sample.forms.SampleImportForm;
import fr.tiogars.starter.sample.forms.SampleInitForm;
import fr.tiogars.starter.sample.forms.SampleUpdateForm;
import fr.tiogars.starter.sample.models.Sample;
import fr.tiogars.starter.sample.models.SampleImportReport;
import fr.tiogars.starter.sample.models.SampleSearchRequest;
import fr.tiogars.starter.sample.models.SampleSearchResponse;
import fr.tiogars.starter.sample.services.SampleCreateService;
import fr.tiogars.starter.sample.services.SampleCrudService;
import fr.tiogars.starter.sample.services.SampleFindService;
import fr.tiogars.starter.sample.services.SampleExportService;
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
    private SampleFindService sampleFindService;

    private SampleUpdateService sampleUpdateService;

    private SampleImportService sampleImportService;

    private SampleExportService sampleExportService;

    private SampleInitService sampleInitService;

    private SampleSearchService sampleSearchService;

    public SampleController(SampleCreateService sampleCreateService, SampleCrudService sampleCrudService,
            SampleUpdateService sampleUpdateService, SampleImportService sampleImportService,
            SampleExportService sampleExportService, SampleInitService sampleInitService, 
            SampleSearchService sampleSearchService, SampleFindService sampleFindService) {
        this.sampleCreateService = sampleCreateService;
        this.sampleCrudService = sampleCrudService;
        this.sampleUpdateService = sampleUpdateService;
        this.sampleImportService = sampleImportService;
        this.sampleExportService = sampleExportService;
        this.sampleInitService = sampleInitService;
        this.sampleSearchService = sampleSearchService;
        this.sampleFindService = sampleFindService;
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
        return this.sampleFindService.findById(id).orElse(null);
    }

    @GetMapping("sample")
    public List<Sample> getAllSamples() {
        return this.sampleFindService.findAll();
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

    @PostMapping(value = "sample/export", produces = {
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "text/csv",
        "application/xml",
        "application/json",
        "application/zip"
    })
    @Operation(summary = "Export samples to file", 
               description = "Export samples in various formats (XLSX, CSV, XML, JSON) with optional ZIP compression. Filename is prefixed with datetime. Returns a file attachment for download.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Export completed successfully. Returns binary file content with appropriate Content-Type and Content-Disposition headers.",
            content = {
                @Content(mediaType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                         schema = @Schema(type = "string", format = "binary", description = "Excel file (XLSX format)")),
                @Content(mediaType = "text/csv", 
                         schema = @Schema(type = "string", format = "binary", description = "CSV file")),
                @Content(mediaType = "application/xml", 
                         schema = @Schema(type = "string", format = "binary", description = "XML file")),
                @Content(mediaType = "application/json", 
                         schema = @Schema(type = "string", format = "binary", description = "JSON file")),
                @Content(mediaType = "application/zip", 
                         schema = @Schema(type = "string", format = "binary", description = "ZIP compressed file"))
            }),
        @ApiResponse(responseCode = "400", description = "Invalid export parameters",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<byte[]> exportSamples(@Valid @RequestBody SampleExportForm form) {
        return this.sampleExportService.exportSamples(form);
    }
}
