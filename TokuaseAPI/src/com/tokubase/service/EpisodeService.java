package com.tokubase.service;

import com.tokubase.dto.episode.CreateEpisodeRequest;
import com.tokubase.dto.episode.EpisodeDetailDTO;
import com.tokubase.dto.episode.EpisodeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface EpisodeService {

    EpisodeResponseDTO createEpisode(CreateEpisodeRequest request);

    EpisodeResponseDTO getEpisodeById(Long id);

    EpisodeDetailDTO getEpisodeDetail(Long id);

    List<EpisodeResponseDTO> getEpisodesBySeries(Long seriesId);

    EpisodeResponseDTO getEpisodeByNumber(Long seriesId, Integer episodeNumber);

    EpisodeResponseDTO updateEpisode(Long id, CreateEpisodeRequest request);

    void deleteEpisode(Long id);

    void deleteAllEpisodesBySeries(Long seriesId);

    // ── Search ────────────────────────────────────────────────────────────

    Page<EpisodeResponseDTO> searchEpisodes(Long seriesId, String title,
                                             LocalDate fromDate, LocalDate toDate,
                                             Pageable pageable);
}