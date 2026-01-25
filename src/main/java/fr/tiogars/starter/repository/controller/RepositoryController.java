package fr.tiogars.starter.repository.controller;

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

import fr.tiogars.starter.repository.models.Repository;
import fr.tiogars.starter.repository.services.RepositoryService;
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
    private final RepositoryService repositoryService;
    public RepositoryController(RepositoryService repositoryService) { this.repositoryService = repositoryService; }

    @GetMapping
    @Operation(summary = "Get all repositories")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Repository.class)))
    public ResponseEntity<List<Repository>> getAllRepositories() { 
        return ResponseEntity.ok(repositoryService.findAll()); 
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get repository by id")
    public ResponseEntity<Repository> getRepositoryById(@PathVariable Long id) {
        return repositoryService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new repository")
    public ResponseEntity<Repository> createRepository(@Valid @RequestBody Repository repository) { 
        return ResponseEntity.ok(repositoryService.create(repository)); 
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a repository")
    public ResponseEntity<Void> deleteRepository(@PathVariable Long id) { 
        repositoryService.deleteById(id); 
        return ResponseEntity.noContent().build(); 
    }
}
