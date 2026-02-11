package com.tokubase.repository;

import com.tokubase.model.Series;
import com.tokubase.model.SeriesType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    List<Series> findByType(SeriesType type);

    boolean existsByName(String name);
}
