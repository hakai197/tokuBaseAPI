package com.tokubase.mapper;

import com.tokubase.dto.character.CreateCharacterRequest;
import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.model.Character;

public class CharacterMapper {

    private CharacterMapper() {}

    public static Character toEntity(CreateCharacterRequest request) {
        return Character.builder()
                .name(request.getName())
                .role(request.getRole())
                .color(request.getColor())
                .build();
    }

    public static CharacterResponseDTO toDTO(Character character) {
        return CharacterResponseDTO.builder()
                .id(character.getId())
                .name(character.getName())
                .role(character.getRole())
                .color(character.getColor())
                .seriesId(character.getSeries().getId())
                .build();
    }
}
