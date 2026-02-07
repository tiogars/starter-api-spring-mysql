package fr.tiogars.starter.repository.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.starter.repository.models.Repository;
import fr.tiogars.starter.repository.models.RepositorySearchRequest;
import fr.tiogars.starter.repository.models.RepositorySearchResponse;
import fr.tiogars.starter.repository.services.RepositoryCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin
@RestController
@RequestMapping("/repositories")
@Tag(name = "repository", description = "CRUD operations for Repository entities")
public class RepositoryQueryController {
    private final RepositoryCrudService repositoryCrudService;

    public RepositoryQueryController(RepositoryCrudService repositoryCrudService) {
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

    @PostMapping("/search")
    @Operation(summary = "Search repositories")
    public ResponseEntity<RepositorySearchResponse> searchRepositories(@RequestBody RepositorySearchRequest request) {
        List<Repository> items = repositoryCrudService.findAll();
        RepositorySearchResponse response = new RepositorySearchResponse();
        response.setItems(items);
        response.setTotal(items.size());
        return ResponseEntity.ok(response);
    }
}
