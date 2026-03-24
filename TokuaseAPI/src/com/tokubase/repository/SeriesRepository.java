package com.tokubase.repository;

import com.tokubase.model.Series;
import com.tokubase.model.SeriesType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {

    List<Series> findByType(SeriesType type);

    Page<Series> findByType(SeriesType type, Pageable pageable);

    List<Series> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);

    /**
     * Dynamic search across series with all parameters optional.
     * JPQL LOWER/LIKE for case-insensitive name matching.
     */
    @Query("""
        SELECT s FROM Series s
        WHERE (:name     IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:type     IS NULL OR s.type       = :type)
          AND (:yearFrom IS NULL OR s.yearStart  >= :yearFrom)
          AND (:yearTo   IS NULL OR s.yearStart  <= :yearTo)
        """)
    Page<Series> search(
        @Param("name")     String name,
        @Param("type")     SeriesType type,
        @Param("yearFrom") Integer yearFrom,
        @Param("yearTo")   Integer yearTo,
        Pageable pageable
    );
}
