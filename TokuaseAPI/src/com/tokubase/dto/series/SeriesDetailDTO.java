package com.tokubase.dto.series;

import com.tokubase.dto.character.CharacterSummaryDTO;

import java.util.List;

public class SeriesDetailDTO {
    private Long id;
    private String name;
    private String type;
    private int yearStart;
    private int yearEnd;
    private List<CharacterSummaryDTO> characters;
}
