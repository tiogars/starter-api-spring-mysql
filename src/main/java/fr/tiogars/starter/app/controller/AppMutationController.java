package fr.tiogars.starter.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.starter.app.models.App;
import fr.tiogars.starter.app.services.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/apps")
@Tag(name = "app", description = "CRUD operations for Application entities")
public class AppMutationController {
    private final AppService appService;

    public AppMutationController(AppService appService) {
        this.appService = appService;
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
