package fr.tiogars.starter.repository.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.tiogars.starter.repository.forms.RepositoryCreateForm;
import fr.tiogars.starter.repository.forms.RepositoryUpdateForm;
import fr.tiogars.starter.repository.models.Repository;
import fr.tiogars.starter.repository.services.RepositoryCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/repositories")
@Tag(name = "repository", description = "CRUD operations for Repository entities")
public class RepositoryMutationController {
    private final RepositoryCrudService repositoryCrudService;

    public RepositoryMutationController(RepositoryCrudService repositoryCrudService) {
        this.repositoryCrudService = repositoryCrudService;
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
}
