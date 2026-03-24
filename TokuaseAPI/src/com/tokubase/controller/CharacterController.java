package com.tokubase.controller;

import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.dto.character.CreateCharacterRequest;
import com.tokubase.dto.form.FormResponseDTO;
import com.tokubase.model.CharacterRole;
import com.tokubase.service.CharacterService;
import com.tokubase.service.FormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
@Tag(name = "Characters", description = "Character CRUD, role filtering, and form sub-resources")
public class CharacterController {

    private final CharacterService characterService;
    private final FormService      formService;

    // ── List ──────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "List all characters")
    public ResponseEntity<List<CharacterResponseDTO>> getAllCharacters() {
        return ResponseEntity.ok(characterService.getAllCharacters());
    }

    // ── Single resource ───────────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get a character by ID")
    @ApiResponse(responseCode = "404", description = "Character not found")
    public ResponseEntity<CharacterResponseDTO> getCharacterById(@PathVariable Long id) {
        return ResponseEntity.ok(characterService.getCharacterById(id));
    }

    // ── Filter by series ──────────────────────────────────────────────────

    @GetMapping("/series/{seriesId}")
    @Operation(summary = "Get all characters for a series")
    public ResponseEntity<List<CharacterResponseDTO>> getCharactersBySeries(@PathVariable Long seriesId) {
        return ResponseEntity.ok(characterService.getCharactersBySeries(seriesId));
    }

    @GetMapping("/series/{seriesId}/role/{role}")
    @Operation(summary = "Get characters by series and role")
    public ResponseEntity<List<CharacterResponseDTO>> getCharactersBySeriesAndRole(
            @PathVariable Long seriesId,
            @PathVariable CharacterRole role) {
        return ResponseEntity.ok(characterService.getCharactersBySeriesAndRole(seriesId, role));
    }

    // ── Filter by role ────────────────────────────────────────────────────

    @GetMapping("/role/{role}")
    @Operation(summary = "Get all characters with the given role (MAIN, SECONDARY, EXTRA, SIXTH)")
    public ResponseEntity<List<CharacterResponseDTO>> getCharactersByRole(@PathVariable CharacterRole role) {
        return ResponseEntity.ok(characterService.getCharactersByRole(role));
    }

    // ── Sub-resource: forms ───────────────────────────────────────────────

    @GetMapping("/{id}/forms")
    @Operation(summary = "Get all forms for a character")
    public ResponseEntity<List<FormResponseDTO>> getFormsByCharacter(@PathVariable Long id) {
        return ResponseEntity.ok(formService.getFormsByCharacter(id));
    }

    @GetMapping("/{id}/forms/final")
    @Operation(summary = "Get only final forms for a character")
    public ResponseEntity<List<FormResponseDTO>> getFinalForms(@PathVariable Long id) {
        return ResponseEntity.ok(formService.getFinalFormsByCharacter(id));
    }

    // ── Search ────────────────────────────────────────────────────────────

    @GetMapping("/search")
    @Operation(summary = "Search characters by name, role, color, and/or series")
    public ResponseEntity<Page<CharacterResponseDTO>> searchCharacters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) CharacterRole role,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Long seriesId,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(
                characterService.searchCharacters(name, role, color, seriesId, pageable));
    }

    // ── Write ─────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new character")
    @ApiResponse(responseCode = "201", description = "Character created")
    @ApiResponse(responseCode = "409", description = "Character already exists in this series")
    public ResponseEntity<CharacterResponseDTO> createCharacter(
            @Valid @RequestBody CreateCharacterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(characterService.createCharacter(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing character (partial – null fields are ignored)")
    @ApiResponse(responseCode = "404", description = "Character not found")
    public ResponseEntity<CharacterResponseDTO> updateCharacter(
            @PathVariable Long id,
            @RequestBody CreateCharacterRequest request) {
        return ResponseEntity.ok(characterService.updateCharacter(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a character (cascades to its forms)")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Character not found")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}