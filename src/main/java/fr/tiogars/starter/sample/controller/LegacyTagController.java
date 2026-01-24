package fr.tiogars.starter.sample.controller;

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

import fr.tiogars.starter.tag.services.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * Controller for managing sample tags (legacy backward-compatible endpoint)
 * Delegates to the new TagService in fr.tiogars.starter.tag domain
 */
@CrossOrigin
@RestController
@RequestMapping("/sample-tag")
@io.swagger.v3.oas.annotations.tags.Tag(name = "tag", description = "CRUD operations for Tag entities (legacy route)")
public class LegacyTagController {
    private final TagService tagService;

    public LegacyTagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @Operation(summary = "Get all sample tags", description = "Retrieves all available sample tags")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tags retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = fr.tiogars.starter.tag.models.Tag.class)))
    })
    public ResponseEntity<List<fr.tiogars.starter.tag.models.Tag>> getAllTags() {
        List<fr.tiogars.starter.tag.models.Tag> tags = tagService.findAll();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sample tag by id", description = "Retrieves a specific sample tag by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = fr.tiogars.starter.tag.models.Tag.class))),
        @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ResponseEntity<fr.tiogars.starter.tag.models.Tag> getTagById(@PathVariable Long id) {
        return tagService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new sample tag", description = "Creates a new sample tag with validation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = fr.tiogars.starter.tag.models.Tag.class)))
    })
    public ResponseEntity<fr.tiogars.starter.tag.models.Tag> createTag(@Valid @RequestBody fr.tiogars.starter.tag.models.Tag tag) {
        fr.tiogars.starter.tag.models.Tag createdTag = tagService.create(tag);
        return ResponseEntity.ok(createdTag);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a sample tag", description = "Deletes a sample tag by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tag deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
