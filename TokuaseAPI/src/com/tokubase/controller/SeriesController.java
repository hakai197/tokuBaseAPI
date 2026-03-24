package com.tokubase.controller;

import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.dto.character.CharacterSummaryDTO;
import com.tokubase.dto.episode.EpisodeResponseDTO;
import com.tokubase.dto.series.CreateSeriesRequest;
import com.tokubase.dto.series.SeriesDetailDTO;
import com.tokubase.dto.series.SeriesResponseDTO;
import com.tokubase.model.SeriesType;
import com.tokubase.service.CharacterService;
import com.tokubase.service.EpisodeService;
import com.tokubase.service.SeriesService;
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
@RequestMapping("/api/series")
@RequiredArgsConstructor
@Tag(name = "Series", description = "Tokusatsu series CRUD and sub-resource access")
public class SeriesController {

    private final SeriesService    seriesService;
    private final CharacterService characterService;
    private final EpisodeService   episodeService;

    // ── List / Page ───────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "List all series (unpaged)")
    public ResponseEntity<List<SeriesResponseDTO>> getAllSeries() {
        return ResponseEntity.ok(seriesService.getAllSeries());
    }

    @GetMapping("/paged")
    @Operation(summary = "List all series with pagination")
    public ResponseEntity<Page<SeriesResponseDTO>> getAllSeriesPaged(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(seriesService.getAllSeriesPaged(pageable));
    }

    // ── Single resource ───────────────────────────────────────────────────

    @GetMapping("/{id}")
    @Operation(summary = "Get a series by ID (summary)")
    @ApiResponse(responseCode = "404", description = "Series not found")
    public ResponseEntity<SeriesResponseDTO> getSeriesById(@PathVariable Long id) {
        return ResponseEntity.ok(seriesService.getSeriesById(id));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Get a series with its full character and episode lists")
    @ApiResponse(responseCode = "404", description = "Series not found")
    public ResponseEntity<SeriesDetailDTO> getSeriesDetail(@PathVariable Long id) {
        return ResponseEntity.ok(seriesService.getSeriesDetail(id));
    }

    // ── Filter by type ────────────────────────────────────────────────────

    @GetMapping("/type/{type}")
    @Operation(summary = "Get all series of a given type (RIDER | SENTAI)")
    public ResponseEntity<List<SeriesResponseDTO>> getSeriesByType(@PathVariable SeriesType type) {
        return ResponseEntity.ok(seriesService.getSeriesByType(type));
    }

    @GetMapping("/type/{type}/paged")
    @Operation(summary = "Get series by type with pagination")
    public ResponseEntity<Page<SeriesResponseDTO>> getSeriesByTypePaged(
            @PathVariable SeriesType type,
            @PageableDefault(size = 20, sort = "yearStart") Pageable pageable) {
        return ResponseEntity.ok(seriesService.getSeriesByTypePaged(type, pageable));
    }

    // ── Sub-resources: characters & episodes ─────────────────────────────

    @GetMapping("/{id}/characters")
    @Operation(summary = "Get all characters belonging to a series")
    public ResponseEntity<List<CharacterResponseDTO>> getCharactersBySeries(@PathVariable Long id) {
        return ResponseEntity.ok(characterService.getCharactersBySeries(id));
    }

    @GetMapping("/{id}/characters/summary")
    @Operation(summary = "Get lightweight character summaries for a series")
    public ResponseEntity<List<CharacterSummaryDTO>> getCharacterSummariesBySeries(@PathVariable Long id) {
        return ResponseEntity.ok(characterService.getCharacterSummariesBySeries(id));
    }

    @GetMapping("/{id}/episodes")
    @Operation(summary = "Get all episodes belonging to a series")
    public ResponseEntity<List<EpisodeResponseDTO>> getEpisodesBySeries(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.getEpisodesBySeries(id));
    }

    // ── Search ────────────────────────────────────────────────────────────

    @GetMapping("/search")
    @Operation(summary = "Search series by name, type, and/or year range")
    public ResponseEntity<Page<SeriesResponseDTO>> searchSeries(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) SeriesType type,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(seriesService.searchSeries(name, type, yearFrom, yearTo, pageable));
    }

    // ── Write ─────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new series")
    @ApiResponse(responseCode = "201", description = "Series created")
    @ApiResponse(responseCode = "409", description = "Series name already exists")
    public ResponseEntity<SeriesResponseDTO> createSeries(@Valid @RequestBody CreateSeriesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seriesService.createSeries(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing series (partial update – null fields are ignored)")
    @ApiResponse(responseCode = "404", description = "Series not found")
    public ResponseEntity<SeriesResponseDTO> updateSeries(
            @PathVariable Long id,
            @RequestBody CreateSeriesRequest request) {
        return ResponseEntity.ok(seriesService.updateSeries(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a series (cascades to characters, forms, and episodes)")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Series not found")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        seriesService.deleteSeries(id);
        return ResponseEntity.noContent().build();
    }
}
