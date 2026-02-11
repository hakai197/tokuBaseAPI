package com.tokubase.repository;

import com.tokubase.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharacterRepository extends JpaRepository<Character, Long> {

    List<Character> findBySeriesId(Long seriesId);

    boolean existsByNameAndSeriesId(String name, Long seriesId);
}
