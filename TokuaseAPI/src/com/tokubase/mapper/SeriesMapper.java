package com.tokubase.mapper;

import com.tokubase.dto.series.CreateSeriesRequest;
import com.tokubase.dto.series.SeriesDetailDTO;
import com.tokubase.dto.series.SeriesResponseDTO;
import com.tokubase.model.Series;

import java.util.stream.Collectors;

public class SeriesMapper {

    private SeriesMapper() {}

    // ── Entity → DTO ──────────────────────────────────────────────────────

    public static SeriesResponseDTO toDTO(Series series) {
        return SeriesResponseDTO.builder()
                .id(series.getId())
                .name(series.getName())
                .type(series.getType())
                .yearStart(series.getYearStart())
                .yearEnd(series.getYearEnd())
                .description(series.getDescription())
                .logoBase64(series.getLogoBase64())
                .posterBase64(series.getPosterBase64())
                .bannerBase64(series.getBannerBase64())
                .seriesImageBase64(series.getSeriesImageBase64())
                .thumbnailBase64(series.getThumbnailBase64())
                .seriesImageUrl(series.getSeriesImageUrl())
                .build();
    }

    /** Full detail DTO – maps nested characters and episodes lists. */
    public static SeriesDetailDTO toDetailDTO(Series series) {
        return SeriesDetailDTO.builder()
                .id(series.getId())
                .name(series.getName())
                .type(series.getType())
                .yearStart(series.getYearStart())
                .yearEnd(series.getYearEnd())
                .description(series.getDescription())
                .logoBase64(series.getLogoBase64())
                .posterBase64(series.getPosterBase64())
                .bannerBase64(series.getBannerBase64())
                .seriesImageBase64(series.getSeriesImageBase64())
                .thumbnailBase64(series.getThumbnailBase64())
                .seriesImageUrl(series.getSeriesImageUrl())
                .characters(series.getCharacters().stream()
                        .map(CharacterMapper::toSummaryDTO)
                        .collect(Collectors.toList()))
                .episodes(series.getEpisodes().stream()
                        .map(EpisodeMapper::toDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    // ── Request → Entity ──────────────────────────────────────────────────

    public static Series toEntity(CreateSeriesRequest request) {
        return Series.builder()
                .name(request.getName())
                .type(request.getType())
                .yearStart(request.getYearStart())
                .yearEnd(request.getYearEnd())
                .description(request.getDescription())
                .logoBase64(request.getLogoBase64())
                .posterBase64(request.getPosterBase64())
                .bannerBase64(request.getBannerBase64())
                .seriesImageBase64(request.getSeriesImageBase64())
                .thumbnailBase64(request.getThumbnailBase64())
                .seriesImageUrl(request.getSeriesImageUrl())
                .build();
    }

    /**
     * Applies a CreateSeriesRequest onto an existing Series entity (used for PUT/update).
     * Only non-null fields from the request overwrite the existing value.
     */
    public static void applyUpdate(CreateSeriesRequest request, Series series) {
        if (request.getName()           != null) series.setName(request.getName());
        if (request.getType()           != null) series.setType(request.getType());
        if (request.getYearStart()      != null) series.setYearStart(request.getYearStart());
        if (request.getYearEnd()        != null) series.setYearEnd(request.getYearEnd());
        if (request.getDescription()    != null) series.setDescription(request.getDescription());
        if (request.getLogoBase64()     != null) series.setLogoBase64(request.getLogoBase64());
        if (request.getPosterBase64()   != null) series.setPosterBase64(request.getPosterBase64());
        if (request.getBannerBase64()   != null) series.setBannerBase64(request.getBannerBase64());
        if (request.getSeriesImageBase64() != null) series.setSeriesImageBase64(request.getSeriesImageBase64());
        if (request.getThumbnailBase64()   != null) series.setThumbnailBase64(request.getThumbnailBase64());
        if (request.getSeriesImageUrl()    != null) series.setSeriesImageUrl(request.getSeriesImageUrl());
    }
}