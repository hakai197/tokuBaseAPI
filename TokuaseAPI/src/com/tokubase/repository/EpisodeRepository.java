package com.tokubase.repository;

import com.tokubase.model.Episode;
import com.tokubase.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findBySeriesId(Long seriesId);
    Optional<Episode> findBySeriesIdAndEpisodeNumber(Long seriesId, Integer episodeNumber);
    boolean existsBySeriesIdAndEpisodeNumber(Long seriesId, Integer episodeNumber);
    void deleteBySeriesId(Long seriesId);
}