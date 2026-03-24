package com.tokubase.repository;

import com.tokubase.model.Episode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    List<Episode> findBySeriesId(Long seriesId);

    Page<Episode> findBySeriesId(Long seriesId, Pageable pageable);

    Optional<Episode> findBySeriesIdAndEpisodeNumber(Long seriesId, Integer episodeNumber);

    List<Episode> findByTitleContainingIgnoreCase(String title);

    boolean existsBySeriesIdAndEpisodeNumber(Long seriesId, Integer episodeNumber);

    void deleteBySeriesId(Long seriesId);

    /** Dynamic episode search – all filters optional. */
    @Query("""
        SELECT e FROM Episode e
        WHERE (:seriesId IS NULL OR e.series.id         = :seriesId)
          AND (:title    IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%')))
          AND (:fromDate IS NULL OR e.airDate            >= :fromDate)
          AND (:toDate   IS NULL OR e.airDate            <= :toDate)
        """)
    Page<Episode> search(
        @Param("seriesId") Long seriesId,
        @Param("title")    String title,
        @Param("fromDate") LocalDate fromDate,
        @Param("toDate")   LocalDate toDate,
        Pageable pageable
    );
}