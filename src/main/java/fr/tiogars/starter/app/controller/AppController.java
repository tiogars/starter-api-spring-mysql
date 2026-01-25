package fr.tiogars.starter.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.starter.app.models.App;
import fr.tiogars.starter.app.services.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/apps")
@Tag(name = "app", description = "CRUD operations for Application entities")
public class AppController {
    private final AppService appService;
    public AppController(AppService appService) { this.appService = appService; }

    @GetMapping
    @Operation(summary = "Get all applications")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = App.class)))
    public ResponseEntity<List<App>> getAllApps() { 
        return ResponseEntity.ok(appService.findAll()); 
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application by id")
    public ResponseEntity<App> getAppById(@PathVariable Long id) {
        return appService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new application")
    public ResponseEntity<App> createApp(@Valid @RequestBody App app) { 
        return ResponseEntity.ok(appService.create(app)); 
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an application")
    public ResponseEntity<Void> deleteApp(@PathVariable Long id) { 
        appService.deleteById(id); 
        return ResponseEntity.noContent().build(); 
    }
}
