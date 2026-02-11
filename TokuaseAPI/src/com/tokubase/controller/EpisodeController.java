package com.tokubase.controller;

import com.tokubase.dto.episode.CreateEpisodeRequest;
import com.tokubase.dto.episode.EpisodeResponseDTO;
import com.tokubase.dto.episode.EpisodeDetailDTO;
import com.tokubase.service.EpisodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/episodes")
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;

    @PostMapping
    public ResponseEntity<EpisodeResponseDTO> createEpisode(@Valid @RequestBody CreateEpisodeRequest request) {
        EpisodeResponseDTO createdEpisode = episodeService.createEpisode(request);
        return new ResponseEntity<>(createdEpisode, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EpisodeResponseDTO> getEpisodeById(@PathVariable Long id) {
        EpisodeResponseDTO episode = episodeService.getEpisodeById(id);
        return ResponseEntity.ok(episode);
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<EpisodeDetailDTO> getEpisodeDetail(@PathVariable Long id) {
        EpisodeDetailDTO episodeDetail = episodeService.getEpisodeDetail(id);
        return ResponseEntity.ok(episodeDetail);
    }

    @GetMapping("/series/{seriesId}")
    public ResponseEntity<List<EpisodeResponseDTO>> getEpisodesBySeries(@PathVariable Long seriesId) {
        List<EpisodeResponseDTO> episodes = episodeService.getEpisodesBySeries(seriesId);
        return ResponseEntity.ok(episodes);
    }

    @GetMapping("/series/{seriesId}/number/{episodeNumber}")
    public ResponseEntity<EpisodeResponseDTO> getEpisodeByNumber(
            @PathVariable Long seriesId,
            @PathVariable Integer episodeNumber) {
        EpisodeResponseDTO episode = episodeService.getEpisodeByNumber(seriesId, episodeNumber);
        return ResponseEntity.ok(episode);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EpisodeResponseDTO> updateEpisode(
            @PathVariable Long id,
            @Valid @RequestBody CreateEpisodeRequest request) {
        EpisodeResponseDTO updatedEpisode = episodeService.updateEpisode(id, request);
        return ResponseEntity.ok(updatedEpisode);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
        episodeService.deleteEpisode(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/series/{seriesId}")
    public ResponseEntity<Void> deleteAllEpisodesBySeries(@PathVariable Long seriesId) {
        episodeService.deleteAllEpisodesBySeries(seriesId);
        return ResponseEntity.noContent().build();
    }
}