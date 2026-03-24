package com.tokubase.service;

import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.dto.episode.EpisodeResponseDTO;
import com.tokubase.dto.search.GlobalSearchResponseDTO;
import com.tokubase.dto.series.SeriesResponseDTO;
import com.tokubase.mapper.CharacterMapper;
import com.tokubase.mapper.EpisodeMapper;
import com.tokubase.mapper.SeriesMapper;
import com.tokubase.repository.CharacterRepository;
import com.tokubase.repository.EpisodeRepository;
import com.tokubase.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final SeriesRepository    seriesRepository;
    private final CharacterRepository characterRepository;
    private final EpisodeRepository   episodeRepository;

    /**
     * Runs three parallel name/title substring queries and aggregates the results.
     * Uses the dedicated findByXxxContainingIgnoreCase methods so each query is
     * a single, indexed LIKE '%query%' call rather than a full-table scan.
     */
    @Override
    @Transactional(readOnly = true)
    public GlobalSearchResponseDTO globalSearch(String query, int limit) {
        if (query == null || query.isBlank()) {
            return GlobalSearchResponseDTO.builder()
                    .query(query)
                    .series(List.of())
                    .characters(List.of())
                    .episodes(List.of())
                    .totalSeries(0)
                    .totalCharacters(0)
                    .totalEpisodes(0)
                    .build();
        }

        // Clamp limit to a sensible range
        int clampedLimit = Math.min(Math.max(limit, 1), 100);
        Pageable top = PageRequest.of(0, clampedLimit);

        // ── Series ────────────────────────────────────────────────────────
        List<SeriesResponseDTO> seriesResults = seriesRepository
                .search(query, null, null, null, top)
                .stream()
                .map(SeriesMapper::toDTO)
                .toList();

        // ── Characters ────────────────────────────────────────────────────
        List<CharacterResponseDTO> characterResults = characterRepository
                .search(query, null, null, null, top)
                .stream()
                .map(CharacterMapper::toDTO)
                .toList();

        // ── Episodes ──────────────────────────────────────────────────────
        List<EpisodeResponseDTO> episodeResults = episodeRepository
                .search(null, query, null, null, top)
                .stream()
                .map(EpisodeMapper::toDTO)
                .toList();

        return GlobalSearchResponseDTO.builder()
                .query(query)
                .series(seriesResults)
                .characters(characterResults)
                .episodes(episodeResults)
                .totalSeries(seriesResults.size())
                .totalCharacters(characterResults.size())
                .totalEpisodes(episodeResults.size())
                .build();
    }
}

