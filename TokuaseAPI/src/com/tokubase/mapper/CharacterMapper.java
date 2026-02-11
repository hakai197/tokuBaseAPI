package com.tokubase.mapper;

import com.tokubase.dto.character.CreateCharacterRequest;
import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.model.Character;

public class CharacterMapper {

    private CharacterMapper() {}

    public static Character toEntity(CreateCharacterRequest request) {
        Character.CharacterBuilder builder = Character.builder()
                .name(request.getName())
                .role(request.getRole())
                .color(request.getColor());

        // Map image fields if present
        if (request.getCharacterImageBase64() != null) {
            builder.characterImageBase64(request.getCharacterImageBase64());
        }
        if (request.getPortraitBase64() != null) {
            builder.portraitBase64(request.getPortraitBase64());
        }
        if (request.getCharacterImageUrl() != null) {
            builder.characterImageUrl(request.getCharacterImageUrl());
        }
        if (request.getThumbnailBase64() != null) {
            builder.thumbnailBase64(request.getThumbnailBase64());
        }

        return builder.build();
    }

    public static CharacterResponseDTO toDTO(Character character) {
        CharacterResponseDTO.CharacterResponseDTOBuilder builder = CharacterResponseDTO.builder()
                .id(character.getId())
                .name(character.getName())
                .role(character.getRole())
                .color(character.getColor())
                .seriesId(character.getSeries().getId());

        // Map image fields
        builder.characterImageBase64(character.getCharacterImageBase64())
                .portraitBase64(character.getPortraitBase64())
                .characterImageUrl(character.getCharacterImageUrl())
                .thumbnailBase64(character.getThumbnailBase64());

        return builder.build();
    }
}