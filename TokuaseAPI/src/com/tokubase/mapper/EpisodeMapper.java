package com.tokubase.mapper;

import com.tokubase.dto.episode.CreateEpisodeRequest;
import com.tokubase.dto.episode.EpisodeResponseDTO;
import com.tokubase.dto.episode.EpisodeDetailDTO;
import com.tokubase.model.Episode;
import com.tokubase.model.Series;

public class EpisodeMapper {

    private EpisodeMapper() {}

    public static Episode toEntity(CreateEpisodeRequest request, Series series) {
        return Episode.builder()
                .episodeNumber(request.getEpisodeNumber())
                .title(request.getTitle())
                .airDate(request.getAirDate())
                .series(series)
                .imageBase64(request.getImageBase64())
                .thumbnailBase64(request.getThumbnailBase64())
                .imageUrl(request.getImageUrl())
                .build();
    }

    public static EpisodeResponseDTO toDTO(Episode episode) {
        return EpisodeResponseDTO.builder()
                .id(episode.getId())
                .episodeNumber(episode.getEpisodeNumber())
                .title(episode.getTitle())
                .airDate(episode.getAirDate())
                .seriesId(episode.getSeries().getId())
                .seriesName(episode.getSeries().getName())
                .imageBase64(episode.getImageBase64())
                .thumbnailBase64(episode.getThumbnailBase64())
                .imageUrl(episode.getImageUrl())
                .build();
    }

    public static EpisodeDetailDTO toDetailDTO(Episode episode) {
        return EpisodeDetailDTO.builder()
                .id(episode.getId())
                .episodeNumber(episode.getEpisodeNumber())
                .title(episode.getTitle())
                .airDate(episode.getAirDate())
                .series(EpisodeDetailDTO.SeriesSummaryDTO.builder()
                        .id(episode.getSeries().getId())
                        .name(episode.getSeries().getName())
                        .type(episode.getSeries().getType())
                        .build())
                .imageBase64(episode.getImageBase64())
                .thumbnailBase64(episode.getThumbnailBase64())
                .imageUrl(episode.getImageUrl())
                .build();
    }
}