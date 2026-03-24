package com.tokubase.service;

import com.tokubase.dto.series.CreateSeriesRequest;
import com.tokubase.dto.series.SeriesDetailDTO;
import com.tokubase.dto.series.SeriesResponseDTO;
import com.tokubase.model.SeriesType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeriesService {

    // ── Read ─────────────────────────────────────────────────────────────

    List<SeriesResponseDTO> getAllSeries();

    Page<SeriesResponseDTO> getAllSeriesPaged(Pageable pageable);

    SeriesResponseDTO getSeriesById(Long id);

    SeriesDetailDTO getSeriesDetail(Long id);

    List<SeriesResponseDTO> getSeriesByType(SeriesType type);

    Page<SeriesResponseDTO> getSeriesByTypePaged(SeriesType type, Pageable pageable);

    // ── Write ─────────────────────────────────────────────────────────────

    SeriesResponseDTO createSeries(CreateSeriesRequest request);

    SeriesResponseDTO updateSeries(Long id, CreateSeriesRequest request);

    void deleteSeries(Long id);

    // ── Search ────────────────────────────────────────────────────────────

    Page<SeriesResponseDTO> searchSeries(String name, SeriesType type,
                                         Integer yearFrom, Integer yearTo,
                                         Pageable pageable);
}
