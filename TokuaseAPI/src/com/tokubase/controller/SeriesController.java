package com.tokubase.controller;

import com.tokubase.dto.series.CreateSeriesRequest;
import com.tokubase.dto.series.SeriesResponseDTO;
import com.tokubase.service.SeriesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/series")
@RequiredArgsConstructor
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping
    public List<SeriesResponseDTO> getAllSeries() {
        return seriesService.getAllSeries();
    }

    @GetMapping("/{id}")
    public SeriesResponseDTO getSeriesById(@PathVariable Long id) {
        return seriesService.getSeriesById(id);
    }

    @PostMapping
    public SeriesResponseDTO createSeries(
            @Valid @RequestBody CreateSeriesRequest request) {
        return seriesService.createSeries(request);
    }
    @DeleteMapping("/{id}")
    public void deleteSeries(@PathVariable Long id) {
        seriesService.deleteSeries(id);
    }

}
