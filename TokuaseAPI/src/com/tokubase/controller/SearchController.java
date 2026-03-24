package com.tokubase.controller;
import com.tokubase.dto.search.GlobalSearchResponseDTO;
import com.tokubase.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Global cross-entity text search")
public class SearchController {
    private final SearchService searchService;
    /**
     * GET /api/search?q=kuuga&limit=10
     * Returns matching series, characters, and episodes in a single response.
     */
    @GetMapping
    @Operation(
        summary = "Global search across series, characters, and episodes",
        description = "Case-insensitive substring match on series name, character name, and episode title."
    )
    public ResponseEntity<GlobalSearchResponseDTO> search(
            @Parameter(description = "Search term (substring, case-insensitive)", required = true)
            @RequestParam String q,
            @Parameter(description = "Max results per entity type (1-100, default 10)")
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(searchService.globalSearch(q, limit));
    }
}
