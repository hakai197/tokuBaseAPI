package com.tokubase.controller;

import com.tokubase.dto.episode.CreateEpisodeRequest;
import com.tokubase.dto.episode.EpisodeDetailDTO;
import com.tokubase.dto.episode.EpisodeResponseDTO;
import com.tokubase.service.EpisodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@RequiredArgsConstructor
@Tag(name = "Episodes", description = "Episode CRUD and lookup by series / episode number")
public class EpisodeController {

    private final EpisodeService episodeService;

    // ── Read ──────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get an episode by ID")
    @ApiResponse(responseCode = "404", description = "Episode not found")
    public ResponseEntity<EpisodeResponseDTO> getEpisodeById(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.getEpisodeById(id));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Get an episode with embedded series info")
    @ApiResponse(responseCode = "404", description = "Episode not found")
    public ResponseEntity<EpisodeDetailDTO> getEpisodeDetail(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.getEpisodeDetail(id));
    }

    @GetMapping("/series/{seriesId}")
    @Operation(summary = "Get all episodes for a series")
    public ResponseEntity<List<EpisodeResponseDTO>> getEpisodesBySeries(@PathVariable Long seriesId) {
        return ResponseEntity.ok(episodeService.getEpisodesBySeries(seriesId));
    }

    @GetMapping("/series/{seriesId}/number/{episodeNumber}")
    @Operation(summary = "Get a specific episode by series ID and episode number")
    @ApiResponse(responseCode = "404", description = "Episode not found")
    public ResponseEntity<EpisodeResponseDTO> getEpisodeByNumber(
            @PathVariable Long seriesId,
            @PathVariable Integer episodeNumber) {
        return ResponseEntity.ok(episodeService.getEpisodeByNumber(seriesId, episodeNumber));
    }

    // ── Search ────────────────────────────────────────────────────────────

    @GetMapping("/search")
    @Operation(summary = "Search episodes by series, title, and/or air-date range")
    public ResponseEntity<Page<EpisodeResponseDTO>> searchEpisodes(
            @RequestParam(required = false) Long seriesId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @PageableDefault(size = 20, sort = "episodeNumber") Pageable pageable) {
        return ResponseEntity.ok(
                episodeService.searchEpisodes(seriesId, title, fromDate, toDate, pageable));
    }

    // ── Write ─────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new episode")
    @ApiResponse(responseCode = "201", description = "Episode created")
    @ApiResponse(responseCode = "409", description = "Episode number already exists for this series")
    public ResponseEntity<EpisodeResponseDTO> createEpisode(@Valid @RequestBody CreateEpisodeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(episodeService.createEpisode(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an episode")
    @ApiResponse(responseCode = "404", description = "Episode not found")
    public ResponseEntity<EpisodeResponseDTO> updateEpisode(
            @PathVariable Long id,
            @Valid @RequestBody CreateEpisodeRequest request) {
        return ResponseEntity.ok(episodeService.updateEpisode(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a single episode")
    @ApiResponse(responseCode = "204", description = "Deleted")
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
        episodeService.deleteEpisode(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/series/{seriesId}")
    @Operation(summary = "Delete ALL episodes for a series")
    @ApiResponse(responseCode = "204", description = "Deleted")
    public ResponseEntity<Void> deleteAllEpisodesBySeries(@PathVariable Long seriesId) {
        episodeService.deleteAllEpisodesBySeries(seriesId);
        return ResponseEntity.noContent().build();
    }
}