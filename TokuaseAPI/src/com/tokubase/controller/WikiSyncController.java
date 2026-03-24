package com.tokubase.controller;

import com.tokubase.dto.wiki.WikiSyncResultDTO;
import com.tokubase.service.WikiSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Admin endpoints that trigger on-demand synchronisation of series and character
 * data from the Fandom wikis.
 *
 * Endpoints (all POST to avoid accidental browser-refresh imports):
 *
 *   POST /api/admin/wiki/sync/rider      – import all Kamen Rider series
 *   POST /api/admin/wiki/sync/sentai     – import all Super Sentai series
 *   POST /api/admin/wiki/sync/all        – import both
 *   POST /api/admin/wiki/sync/characters/{seriesId}  – import characters for one series
 *
 * Attribution: All imported data originates from Fandom wikis and is
 * licenced under Creative Commons Attribution-Share Alike 3.0 (CC-BY-SA 3.0).
 *   • Kamen Rider Wiki  : https://kamenrider.fandom.com
 *   • RangerWiki        : https://powerrangers.fandom.com
 */
@RestController
@RequestMapping("/api/admin/wiki")
@RequiredArgsConstructor
@Tag(name = "Wiki Sync (Admin)", description = "Import series and character data from Kamen Rider Wiki and RangerWiki")
public class WikiSyncController {

    private final WikiSyncService wikiSyncService;

    // ── Series sync ───────────────────────────────────────────────────────

    @PostMapping("/sync/rider")
    @Operation(
        summary = "Sync all Kamen Rider series from kamenrider.fandom.com",
        description = """
            Fetches the Kamen Rider series category from Kamen Rider Wiki,
            imports series name, description, broadcast years, and thumbnail URL.
            Existing series (matched by name) have their image URL refreshed only.
            New series are created. Returns a summary of the import.
            """
    )
    @ApiResponse(responseCode = "200", description = "Sync complete – see result body for counts")
    public ResponseEntity<WikiSyncResultDTO> syncRider() {
        return ResponseEntity.ok(wikiSyncService.syncRiderSeries());
    }

    @PostMapping("/sync/sentai")
    @Operation(
        summary = "Sync all Super Sentai series from powerrangers.fandom.com",
        description = "Fetches the Super Sentai series category from RangerWiki and imports data."
    )
    @ApiResponse(responseCode = "200", description = "Sync complete")
    public ResponseEntity<WikiSyncResultDTO> syncSentai() {
        return ResponseEntity.ok(wikiSyncService.syncSentaiSeries());
    }

    @PostMapping("/sync/all")
    @Operation(
        summary = "Sync ALL series (Kamen Rider + Super Sentai)",
        description = "Runs Rider sync followed by Sentai sync and returns a merged result."
    )
    @ApiResponse(responseCode = "200", description = "Full sync complete")
    public ResponseEntity<WikiSyncResultDTO> syncAll() {
        return ResponseEntity.ok(wikiSyncService.syncAll());
    }

    // ── Character sync ────────────────────────────────────────────────────

    @PostMapping("/sync/characters/{seriesId}")
    @Operation(
        summary = "Sync characters for a specific series",
        description = """
            Looks up the '{SeriesName}_characters' category on the appropriate wiki
            and imports character names and thumbnail URLs.
            The series must already exist in the database (run a series sync first).
            """
    )
    @ApiResponse(responseCode = "200", description = "Character sync complete")
    @ApiResponse(responseCode = "404", description = "Series not found")
    public ResponseEntity<WikiSyncResultDTO> syncCharacters(@PathVariable Long seriesId) {
        return ResponseEntity.ok(wikiSyncService.syncCharactersForSeries(seriesId));
    }
}

