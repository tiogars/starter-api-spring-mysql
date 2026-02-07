package fr.tiogars.starter.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.starter.app.models.App;
import fr.tiogars.starter.app.services.AppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/apps")
@Tag(name = "app", description = "CRUD operations for Application entities")
public class AppQueryController {
    private final AppService appService;

    public AppQueryController(AppService appService) {
        this.appService = appService;
    }

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
}
