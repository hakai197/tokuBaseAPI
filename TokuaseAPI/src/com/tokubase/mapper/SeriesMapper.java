package com.tokubase.mapper;

import com.tokubase.dto.series.CreateSeriesRequest;
import com.tokubase.dto.series.SeriesResponseDTO;
import com.tokubase.model.Series;

public class SeriesMapper {

    private SeriesMapper() {}

    public static Series toEntity(CreateSeriesRequest request) {
        return Series.builder()
                .name(request.getName())
                .type(request.getType())
                .yearStart(request.getYearStart())
                .yearEnd(request.getYearEnd())
                .description(request.getDescription())
                .build();
    }

    public static SeriesResponseDTO toDTO(Series series) {
        return SeriesResponseDTO.builder()
                .id(series.getId())
                .name(series.getName())
                .type(series.getType())
                .yearStart(series.getYearStart())
                .yearEnd(series.getYearEnd())
                .description(series.getDescription())
                .build();
    }
}
