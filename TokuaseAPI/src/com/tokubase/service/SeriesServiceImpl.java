package com.tokubase.service;

import com.tokubase.dto.series.CreateSeriesRequest;
import com.tokubase.dto.series.SeriesDetailDTO;
import com.tokubase.dto.series.SeriesResponseDTO;
import com.tokubase.exception.DuplicateResourceException;
import com.tokubase.exception.ResourceNotFoundException;
import com.tokubase.mapper.SeriesMapper;
import com.tokubase.model.Series;
import com.tokubase.model.SeriesType;
import com.tokubase.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;

    // ── Read ─────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<SeriesResponseDTO> getAllSeries() {
        return seriesRepository.findAll().stream()
                .map(SeriesMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SeriesResponseDTO> getAllSeriesPaged(Pageable pageable) {
        return seriesRepository.findAll(pageable).map(SeriesMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public SeriesResponseDTO getSeriesById(Long id) {
        return SeriesMapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public SeriesDetailDTO getSeriesDetail(Long id) {
        // Characters and episodes are loaded lazily; this method must run inside a tx
        return SeriesMapper.toDetailDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeriesResponseDTO> getSeriesByType(SeriesType type) {
        return seriesRepository.findByType(type).stream()
                .map(SeriesMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SeriesResponseDTO> getSeriesByTypePaged(SeriesType type, Pageable pageable) {
        return seriesRepository.findByType(type, pageable).map(SeriesMapper::toDTO);
    }

    // ── Write ─────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public SeriesResponseDTO createSeries(CreateSeriesRequest request) {
        if (seriesRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "Series already exists with name: " + request.getName());
        }
        Series saved = seriesRepository.save(SeriesMapper.toEntity(request));
        return SeriesMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public SeriesResponseDTO updateSeries(Long id, CreateSeriesRequest request) {
        Series series = findOrThrow(id);

        // If name is changing, check it won't clash with another series
        if (request.getName() != null
                && !request.getName().equalsIgnoreCase(series.getName())
                && seriesRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "Another series already uses the name: " + request.getName());
        }

        SeriesMapper.applyUpdate(request, series);
        return SeriesMapper.toDTO(seriesRepository.save(series));
    }

    @Override
    @Transactional
    public void deleteSeries(Long id) {
        if (!seriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Series not found with id: " + id);
        }
        seriesRepository.deleteById(id);
    }

    // ── Search ────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<SeriesResponseDTO> searchSeries(String name, SeriesType type,
                                                 Integer yearFrom, Integer yearTo,
                                                 Pageable pageable) {
        return seriesRepository.search(name, type, yearFrom, yearTo, pageable)
                .map(SeriesMapper::toDTO);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private Series findOrThrow(Long id) {
        return seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series not found with id: " + id));
    }
}
