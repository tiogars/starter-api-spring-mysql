package fr.tiogars.starter.repository.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import fr.tiogars.starter.repository.models.Repository;
import fr.tiogars.starter.repository.models.RepositorySearchRequest;
import fr.tiogars.starter.repository.models.RepositorySearchResponse;
import fr.tiogars.starter.repository.forms.RepositoryCreateForm;
import fr.tiogars.starter.repository.forms.RepositoryUpdateForm;
import fr.tiogars.starter.repository.services.RepositoryCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/repositories")
@Tag(name = "repository", description = "CRUD operations for Repository entities")
public class RepositoryController {
    private final RepositoryCrudService repositoryCrudService;

    public RepositoryController(RepositoryCrudService repositoryCrudService) {
        this.repositoryCrudService = repositoryCrudService;
    }

    @GetMapping
    @Operation(summary = "Get all repositories")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Repository.class)))
    public ResponseEntity<List<Repository>> getAllRepositories() {
        return ResponseEntity.ok(repositoryCrudService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get repository by id")
    public ResponseEntity<Repository> getRepositoryById(@PathVariable Long id) {
        return repositoryCrudService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new repository")
    public ResponseEntity<Repository> createRepository(@Valid @RequestBody RepositoryCreateForm form) {
        return ResponseEntity.ok(repositoryCrudService.create(form));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a repository")
    public ResponseEntity<Repository> updateRepository(@PathVariable Long id, @Valid @RequestBody RepositoryUpdateForm form) {
        Optional<Repository> updated = repositoryCrudService.update(id, form);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a repository")
    public ResponseEntity<Void> deleteRepository(@PathVariable Long id) {
        repositoryCrudService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    @Operation(summary = "Search repositories")
    public ResponseEntity<RepositorySearchResponse> searchRepositories(@RequestBody RepositorySearchRequest request) {
        // For simplicity, not using pagination here, but can be added as in SampleSearchService
        List<Repository> items = repositoryCrudService.findAll(); // Replace with actual search logic
        RepositorySearchResponse response = new RepositorySearchResponse();
        response.setItems(items);
        response.setTotal(items.size());
        return ResponseEntity.ok(response);
    }
}
