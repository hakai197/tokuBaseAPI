package com.tokubase.repository;

import com.tokubase.model.Character;
import com.tokubase.model.CharacterRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    List<Character> findBySeriesId(Long seriesId);

    Page<Character> findBySeriesId(Long seriesId, Pageable pageable);

    List<Character> findByRole(CharacterRole role);

    List<Character> findBySeriesIdAndRole(Long seriesId, CharacterRole role);

    List<Character> findByNameContainingIgnoreCase(String name);

    boolean existsByNameAndSeriesId(String name, Long seriesId);

    /** Dynamic character search – all filters are optional. */
    @Query("""
        SELECT c FROM Character c
        WHERE (:name     IS NULL OR LOWER(c.name)  LIKE LOWER(CONCAT('%', :name,  '%')))
          AND (:role     IS NULL OR c.role         = :role)
          AND (:color    IS NULL OR LOWER(c.color) LIKE LOWER(CONCAT('%', :color, '%')))
          AND (:seriesId IS NULL OR c.series.id    = :seriesId)
        """)
    Page<Character> search(
        @Param("name")     String name,
        @Param("role")     CharacterRole role,
        @Param("color")    String color,
        @Param("seriesId") Long seriesId,
        Pageable pageable
    );
}
