package com.tokubase.dto.character;

import com.tokubase.dto.series.SeriesDetailDTO;

import java.util.List;

public class CharacterSummaryDTO {
    private long id;
    private String name;
    private List<SeriesDetailDTO> series_id;
    private String role;
    private String color;
    
}
