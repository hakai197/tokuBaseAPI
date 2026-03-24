package com.tokubase.controller;

import com.tokubase.dto.form.CreateFormRequest;
import com.tokubase.dto.form.FormResponseDTO;
import com.tokubase.service.FormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@Tag(name = "Forms", description = "Character form (transformation) CRUD")
public class FormController {

    private final FormService formService;

    // ── Read ──────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get a form by ID")
    @ApiResponse(responseCode = "404", description = "Form not found")
    public ResponseEntity<FormResponseDTO> getFormById(@PathVariable Long id) {
        return ResponseEntity.ok(formService.getFormById(id));
    }

    @GetMapping("/character/{characterId}")
    @Operation(summary = "Get all forms for a character")
    public ResponseEntity<List<FormResponseDTO>> getFormsByCharacter(@PathVariable Long characterId) {
        return ResponseEntity.ok(formService.getFormsByCharacter(characterId));
    }

    @GetMapping("/character/{characterId}/final")
    @Operation(summary = "Get only final forms for a character")
    public ResponseEntity<List<FormResponseDTO>> getFinalFormsByCharacter(@PathVariable Long characterId) {
        return ResponseEntity.ok(formService.getFinalFormsByCharacter(characterId));
    }

    // ── Write ─────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new form")
    @ApiResponse(responseCode = "201", description = "Form created")
    @ApiResponse(responseCode = "409", description = "Form name already exists for this character")
    public ResponseEntity<FormResponseDTO> createForm(@Valid @RequestBody CreateFormRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(formService.createForm(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a form (partial – null fields are ignored)")
    @ApiResponse(responseCode = "404", description = "Form not found")
    public ResponseEntity<FormResponseDTO> updateForm(
            @PathVariable Long id,
            @RequestBody CreateFormRequest request) {
        return ResponseEntity.ok(formService.updateForm(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a form")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Form not found")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
        formService.deleteForm(id);
        return ResponseEntity.noContent().build();
    }
}
