package com.tokubase.service;

import com.tokubase.dto.wiki.WikiPageDetail;
import com.tokubase.dto.wiki.WikiSyncResultDTO;
import com.tokubase.exception.ResourceNotFoundException;
import com.tokubase.model.Character;
import com.tokubase.model.CharacterRole;
import com.tokubase.model.Series;
import com.tokubase.model.SeriesType;
import com.tokubase.repository.CharacterRepository;
import com.tokubase.repository.SeriesRepository;
import com.tokubase.util.WikiApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates importing series and character data from the Fandom wikis into
 * the local MySQL database.
 *
 * Data sources:
 *   - Kamen Rider Wiki  : https://kamenrider.fandom.com  (SeriesType.RIDER)
 *   - RangerWiki        : https://powerrangers.fandom.com (SeriesType.SENTAI)
 *
 * Images are stored as URLs (Fandom CDN) in the seriesImageUrl / characterImageUrl
 * fields. No base64 download is performed during bulk sync to keep memory usage low;
 * individual base64 fields can be populated later via the update endpoints.
 *
 * Attribution: content sourced from Fandom wikis is licenced under CC-BY-SA 3.0.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WikiSyncServiceImpl implements WikiSyncService {

    private final WikiApiClient       wikiApiClient;
    private final SeriesRepository    seriesRepository;
    private final CharacterRepository characterRepository;

    @Value("${tokubase.wiki.rider.api-url}")
    private String riderApiUrl;

    @Value("${tokubase.wiki.rider.series-category}")
    private String riderSeriesCategory;

    @Value("${tokubase.wiki.sentai.api-url}")
    private String sentaiApiUrl;

    @Value("${tokubase.wiki.sentai.series-category}")
    private String sentaiSeriesCategory;

    @Value("${tokubase.wiki.max-series:150}")
    private int maxSeries;

    // ── Public sync operations ────────────────────────────────────────────

    @Override
    @Transactional
    public WikiSyncResultDTO syncRiderSeries() {
        log.info("Starting Kamen Rider series sync from {}", riderApiUrl);
        return syncSeries(riderApiUrl, riderSeriesCategory, SeriesType.RIDER);
    }

    @Override
    @Transactional
    public WikiSyncResultDTO syncSentaiSeries() {
        log.info("Starting Super Sentai series sync from {}", sentaiApiUrl);
        return syncSeries(sentaiApiUrl, sentaiSeriesCategory, SeriesType.SENTAI);
    }

    @Override
    @Transactional
    public WikiSyncResultDTO syncAll() {
        log.info("Starting full wiki sync (RIDER + SENTAI)");
        WikiSyncResultDTO rider  = syncRiderSeries();
        WikiSyncResultDTO sentai = syncSentaiSeries();

        // Merge the two results into one summary
        List<String> allWarnings = new ArrayList<>(rider.getWarnings());
        allWarnings.addAll(sentai.getWarnings());

        return WikiSyncResultDTO.builder()
                .syncedAt(LocalDateTime.now())
                .type("ALL")
                .seriesCreated(rider.getSeriesCreated()      + sentai.getSeriesCreated())
                .seriesSkipped(rider.getSeriesSkipped()      + sentai.getSeriesSkipped())
                .seriesUpdated(rider.getSeriesUpdated()      + sentai.getSeriesUpdated())
                .charactersCreated(rider.getCharactersCreated() + sentai.getCharactersCreated())
                .charactersSkipped(rider.getCharactersSkipped() + sentai.getCharactersSkipped())
                .warnings(allWarnings)
                .message("Full sync complete.")
                .build();
    }

    @Override
    @Transactional
    public WikiSyncResultDTO syncCharactersForSeries(Long seriesId) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Series not found with id: " + seriesId));

        String apiUrl = series.getType() == SeriesType.RIDER ? riderApiUrl : sentaiApiUrl;

        log.info("Syncing characters for series '{}' from {}", series.getName(), apiUrl);

        List<String> warnings  = new ArrayList<>();
        int created  = 0;
        int skipped  = 0;

        // Try both common category name patterns used by the Fandom wikis
        List<String> charTitles = wikiApiClient.getSeriesSubcategoryMembers(
                apiUrl, series.getName(), "_characters", 100);

        if (charTitles.isEmpty()) {
            // Fallback: use the human-form name (e.g. "Kamen_Rider_Kuuga_characters")
            charTitles = wikiApiClient.getSeriesSubcategoryMembers(
                    apiUrl, series.getName().replace(" ", "_"), "_characters", 100);
        }

        if (charTitles.isEmpty()) {
            String msg = "No character category found for series: " + series.getName();
            log.warn(msg);
            warnings.add(msg);
        }

        for (String title : charTitles) {
            if (characterRepository.existsByNameAndSeriesId(title, seriesId)) {
                log.debug("Character already exists: '{}'", title);
                skipped++;
                continue;
            }

            WikiPageDetail detail = wikiApiClient.getPageDetail(apiUrl, title);
            CharacterRole role = deriveCharacterRole(title, series.getType());

            Character character = Character.builder()
                    .name(detail != null ? cleanTitle(detail.getTitle()) : cleanTitle(title))
                    .series(series)
                    .role(role)
                    .characterImageUrl(detail != null ? detail.getThumbnailUrl() : null)
                    .thumbnailBase64(null)     // populated on demand via PUT /{id}
                    .build();

            characterRepository.save(character);
            log.info("  Created character: '{}'", character.getName());
            created++;
        }

        return WikiSyncResultDTO.builder()
                .syncedAt(LocalDateTime.now())
                .type(series.getType().name())
                .seriesCreated(0).seriesSkipped(0).seriesUpdated(0)
                .charactersCreated(created)
                .charactersSkipped(skipped)
                .warnings(warnings)
                .message("Character sync complete for: " + series.getName())
                .build();
    }

    // ── Core series sync ──────────────────────────────────────────────────

    /**
     * Common implementation used by both syncRiderSeries() and syncSentaiSeries().
     *
     * Strategy per page title:
     *   - If a Series with the same name already exists → refresh imageUrl (update)
     *   - Otherwise → create a new Series
     */
    private WikiSyncResultDTO syncSeries(String apiUrl, String category, SeriesType type) {
        List<String> warnings = new ArrayList<>();
        int created = 0, skipped = 0, updated = 0;

        List<String> titles = wikiApiClient.getCategoryMembers(apiUrl, category, maxSeries);

        if (titles.isEmpty()) {
            String msg = "Wiki returned 0 pages for category '" + category
                    + "'. Check the category name in application.properties.";
            log.warn(msg);
            warnings.add(msg);
        }

        for (String rawTitle : titles) {
            String name = cleanTitle(rawTitle);
            if (name.isBlank()) continue;

            try {
                WikiPageDetail detail = wikiApiClient.getPageDetail(apiUrl, rawTitle);

                if (detail == null) {
                    warnings.add("Could not fetch details for: " + rawTitle);
                    continue;
                }

                // Determine year — fall back to a safe default so DB constraint is met
                int yearStart = detail.getYearStart() > 0 ? detail.getYearStart() : 1970;

                // Check for existing record (unique by name)
                List<Series> existing = seriesRepository.findByNameContainingIgnoreCase(name);
                Series match = existing.stream()
                        .filter(s -> s.getName().equalsIgnoreCase(name))
                        .findFirst().orElse(null);

                if (match != null) {
                    // Refresh image URL and description if they were blank
                    boolean changed = false;
                    if (match.getSeriesImageUrl() == null && detail.getThumbnailUrl() != null) {
                        match.setSeriesImageUrl(detail.getThumbnailUrl());
                        changed = true;
                    }
                    if ((match.getDescription() == null || match.getDescription().isBlank())
                            && detail.getExtract() != null) {
                        match.setDescription(detail.getExtract());
                        changed = true;
                    }
                    if (changed) {
                        seriesRepository.save(match);
                        updated++;
                    } else {
                        skipped++;
                    }
                    continue;
                }

                // Create new series
                Series series = Series.builder()
                        .name(name)
                        .type(type)
                        .yearStart(yearStart)
                        .yearEnd(detail.getYearEnd() > 0 ? detail.getYearEnd() : null)
                        .description(detail.getExtract())
                        .seriesImageUrl(detail.getThumbnailUrl())   // Fandom CDN URL
                        .thumbnailBase64(null)                       // populated on demand
                        .build();

                seriesRepository.save(series);
                log.info("  Created {} series: '{}' ({})", type, name, yearStart);
                created++;

            } catch (Exception e) {
                String msg = "Error importing '" + rawTitle + "': " + e.getMessage();
                log.warn(msg);
                warnings.add(msg);
            }
        }

        log.info("Sync complete [{}]: created={}, skipped={}, updated={}, warnings={}",
                type, created, skipped, updated, warnings.size());

        return WikiSyncResultDTO.builder()
                .syncedAt(LocalDateTime.now())
                .type(type.name())
                .seriesCreated(created)
                .seriesSkipped(skipped)
                .seriesUpdated(updated)
                .charactersCreated(0)
                .charactersSkipped(0)
                .warnings(warnings)
                .message(String.format("Sync done: %d created, %d updated, %d skipped.",
                        created, updated, skipped))
                .build();
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    /**
     * Removes wiki disambiguation suffixes, e.g.:
     *   "Kamen Rider (series)" → "Kamen Rider"
     *   "Avataro Sentai Donbrothers" → "Avataro Sentai Donbrothers"
     */
    private String cleanTitle(String rawTitle) {
        if (rawTitle == null) return "";
        // Strip " (disambiguation)" / " (TV series)" / " (film)" style suffixes
        return rawTitle.replaceAll("\\s*\\(.*\\)\\s*$", "").trim();
    }

    /**
     * Infers a character role from the page title and series type.
     *
     * Rules:
     *  SENTAI: titles containing "Red/Black/Blue/Yellow/Pink" → MAIN
     *          titles containing "Gold/Silver/White/Extra" → SIXTH
     *  RIDER:  primary/main Rider keywords → MAIN; others → SECONDARY
     *  Default: MAIN
     */
    private CharacterRole deriveCharacterRole(String title, SeriesType type) {
        String lower = title.toLowerCase();
        if (type == SeriesType.SENTAI) {
            if (lower.contains("red")   || lower.contains("blue")  ||
                lower.contains("black") || lower.contains("yellow") ||
                lower.contains("pink")  || lower.contains("green")) {
                return CharacterRole.MAIN;
            }
            if (lower.contains("gold")   || lower.contains("silver") ||
                lower.contains("white")  || lower.contains("sixth")  ||
                lower.contains("extra")) {
                return CharacterRole.SIXTH;
            }
        }
        // RIDER: if the title starts with "Kamen Rider" it's likely a main rider form,
        // not a named human character – treat as MAIN
        if (lower.startsWith("kamen rider")) return CharacterRole.MAIN;
        return CharacterRole.MAIN; // safe default
    }
}

