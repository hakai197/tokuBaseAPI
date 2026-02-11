package com.tokubase.service;

import com.tokubase.dto.series.CreateSeriesRequest;
import com.tokubase.dto.series.SeriesResponseDTO;

import java.util.List;

public interface SeriesService {

    List<SeriesResponseDTO> getAllSeries();

    SeriesResponseDTO getSeriesById(Long id);

    SeriesResponseDTO createSeries(CreateSeriesRequest request);

    void deleteSeries(Long id);
}
