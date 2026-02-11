package com.tokubase.dto.character;

import com.tokubase.model.CharacterRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterResponseDTO {

    private Long id;
    private String name;
    private CharacterRole role;
    private String color;
    private Long seriesId;
}
