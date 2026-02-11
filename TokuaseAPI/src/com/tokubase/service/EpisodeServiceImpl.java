package com.tokubase.service;

import com.tokubase.dto.episode.CreateEpisodeRequest;
import com.tokubase.dto.episode.EpisodeResponseDTO;
import com.tokubase.dto.episode.EpisodeDetailDTO;
import com.tokubase.exception.DuplicateResourceException;
import com.tokubase.exception.ResourceNotFoundException;
import com.tokubase.mapper.EpisodeMapper;
import com.tokubase.model.Episode;
import com.tokubase.model.Series;
import com.tokubase.repository.EpisodeRepository;
import com.tokubase.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final SeriesRepository seriesRepository;

    @Override
    public EpisodeResponseDTO createEpisode(CreateEpisodeRequest request) {
        Series series = seriesRepository.findById(request.getSeriesId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Series not found with id: " + request.getSeriesId()));

        // Check if episode number already exists for this series
        if (episodeRepository.existsBySeriesIdAndEpisodeNumber(request.getSeriesId(), request.getEpisodeNumber())) {
            throw new DuplicateResourceException(
                    "Episode " + request.getEpisodeNumber() + " already exists for series: " + series.getName());
        }

        Episode episode = EpisodeMapper.toEntity(request, series);
        Episode saved = episodeRepository.save(episode);

        return EpisodeMapper.toDTO(saved);
    }

    @Override
    public EpisodeResponseDTO getEpisodeById(Long id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found with id: " + id));
        return EpisodeMapper.toDTO(episode);
    }

    @Override
    public EpisodeDetailDTO getEpisodeDetail(Long id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found with id: " + id));
        return EpisodeMapper.toDetailDTO(episode);
    }

    @Override
    public List<EpisodeResponseDTO> getEpisodesBySeries(Long seriesId) {
        // Verify series exists
        seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Series not found with id: " + seriesId));

        return episodeRepository.findBySeriesId(seriesId).stream()
                .map(EpisodeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EpisodeResponseDTO getEpisodeByNumber(Long seriesId, Integer episodeNumber) {
        Episode episode = episodeRepository.findBySeriesIdAndEpisodeNumber(seriesId, episodeNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Episode " + episodeNumber + " not found for series id: " + seriesId));
        return EpisodeMapper.toDTO(episode);
    }

    @Override
    @Transactional
    public EpisodeResponseDTO updateEpisode(Long id, CreateEpisodeRequest request) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found with id: " + id));

        // Check if updating to a different episode number that already exists
        if (!episode.getEpisodeNumber().equals(request.getEpisodeNumber()) &&
                episodeRepository.existsBySeriesIdAndEpisodeNumber(episode.getSeries().getId(), request.getEpisodeNumber())) {
            throw new DuplicateResourceException(
                    "Episode " + request.getEpisodeNumber() + " already exists for this series");
        }

        episode.setEpisodeNumber(request.getEpisodeNumber());
        episode.setTitle(request.getTitle());
        episode.setAirDate(request.getAirDate());

        // Update image fields if provided
        if (request.getImageBase64() != null) {
            episode.setImageBase64(request.getImageBase64());
        }
        if (request.getThumbnailBase64() != null) {
            episode.setThumbnailBase64(request.getThumbnailBase64());
        }
        if (request.getImageUrl() != null) {
            episode.setImageUrl(request.getImageUrl());
        }

        Episode updated = episodeRepository.save(episode);
        return EpisodeMapper.toDTO(updated);
    }

    @Override
    public void deleteEpisode(Long id) {
        if (!episodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Episode not found with id: " + id);
        }
        episodeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllEpisodesBySeries(Long seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ResourceNotFoundException("Series not found with id: " + seriesId);
        }
        episodeRepository.deleteBySeriesId(seriesId);
    }
}
