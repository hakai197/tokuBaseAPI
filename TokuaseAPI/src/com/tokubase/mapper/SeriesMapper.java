package com.tokubase.mapper;

import com.tokubase.dto.series.CreateSeriesRequest;
import com.tokubase.dto.series.SeriesResponseDTO;
import com.tokubase.model.Series;

public class SeriesMapper {

    private SeriesMapper() {}

    public static Series toEntity(CreateSeriesRequest request) {
        Series.SeriesBuilder builder = Series.builder()
                .name(request.getName())
                .type(request.getType())
                .yearStart(request.getYearStart())
                .yearEnd(request.getYearEnd())
                .description(request.getDescription());

        // Map image fields if present
        if (request.getLogoBase64() != null) {
            builder.logoBase64(request.getLogoBase64());
        }
        if (request.getPosterBase64() != null) {
            builder.posterBase64(request.getPosterBase64());
        }
        if (request.getBannerBase64() != null) {
            builder.bannerBase64(request.getBannerBase64());
        }
        if (request.getSeriesImageBase64() != null) {
            builder.seriesImageBase64(request.getSeriesImageBase64());
        }
        if (request.getThumbnailBase64() != null) {
            builder.thumbnailBase64(request.getThumbnailBase64());
        }
        if (request.getSeriesImageUrl() != null) {
            builder.seriesImageUrl(request.getSeriesImageUrl());
        }

        return builder.build();
    }

    public static SeriesResponseDTO toDTO(Series series) {
        SeriesResponseDTO.SeriesResponseDTOBuilder builder = SeriesResponseDTO.builder()
                .id(series.getId())
                .name(series.getName())
                .type(series.getType())
                .yearStart(series.getYearStart())
                .yearEnd(series.getYearEnd())
                .description(series.getDescription());

        // Map image fields
        builder.logoBase64(series.getLogoBase64())
                .posterBase64(series.getPosterBase64())
                .bannerBase64(series.getBannerBase64())
                .seriesImageBase64(series.getSeriesImageBase64())
                .thumbnailBase64(series.getThumbnailBase64())
                .seriesImageUrl(series.getSeriesImageUrl());

        return builder.build();
    }
}