package com.tokubase.dto.character;

import com.tokubase.model.CharacterRole;
import lombok.Builder;
import lombok.Getter;

/**
 * Lightweight character summary used inside SeriesDetailDTO and search results.
 */
@Getter
@Builder
public class CharacterSummaryDTO {

    private Long id;
    private String name;
    private CharacterRole role;
    private String color;
    private Long seriesId;
    private String thumbnailBase64;
    private String characterImageUrl;
}
