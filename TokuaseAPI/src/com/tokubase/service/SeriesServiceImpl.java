package com.tokubase.service;

import com.tokubase.dto.series.CreateSeriesRequest;
import com.tokubase.dto.series.SeriesResponseDTO;
import com.tokubase.exception.ResourceNotFoundException;
import com.tokubase.mapper.SeriesMapper;
import com.tokubase.model.Series;
import com.tokubase.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeriesServiceImpl implements SeriesService {

    private final SeriesRepository seriesRepository;

    @Override
    public List<SeriesResponseDTO> getAllSeries() {
        return seriesRepository.findAll()
                .stream()
                .map(SeriesMapper::toDTO)
                .toList();
    }

    @Override
    public SeriesResponseDTO getSeriesById(Long id) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series not found with id: " + id));

        return SeriesMapper.toDTO(series);
    }

    @Override
    public SeriesResponseDTO createSeries(CreateSeriesRequest request) {

        if (seriesRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Series already exists with name: " + request.getName());
        }

        Series series = SeriesMapper.toEntity(request);

        Series saved = seriesRepository.save(series);

        return SeriesMapper.toDTO(saved);
    }

    @Override
    public void deleteSeries(Long id) {

        if (!seriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Series not found with id: " + id);
        }

        seriesRepository.deleteById(id);
    }
}
