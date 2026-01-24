package fr.tiogars.starter.tag.controller;

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

import fr.tiogars.starter.tag.models.Tag;
import fr.tiogars.starter.tag.services.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
// Use fully qualified OAS Tag to avoid name collision with model Tag
import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "tag", description = "CRUD operations for Tag entities")
public class TagController {
    private final TagService tagService;
    public TagController(TagService tagService) { this.tagService = tagService; }

    @GetMapping
    @Operation(summary = "Get all tags")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Tag.class)))
    public ResponseEntity<List<Tag>> getAllTags() { return ResponseEntity.ok(tagService.findAll()); }

    @GetMapping("/{id}")
    @Operation(summary = "Get tag by id")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        return tagService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new tag")
    public ResponseEntity<Tag> createTag(@Valid @RequestBody Tag tag) { return ResponseEntity.ok(tagService.create(tag)); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tag")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) { tagService.deleteById(id); return ResponseEntity.noContent().build(); }
}
