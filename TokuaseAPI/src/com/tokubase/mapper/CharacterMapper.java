package com.tokubase.mapper;

import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.dto.character.CharacterSummaryDTO;
import com.tokubase.dto.character.CreateCharacterRequest;
import com.tokubase.model.Character;

public class CharacterMapper {

    private CharacterMapper() {}

    public static Character toEntity(CreateCharacterRequest request) {
        return Character.builder()
                .name(request.getName())
                .role(request.getRole())
                .color(request.getColor())
                .characterImageBase64(request.getCharacterImageBase64())
                .portraitBase64(request.getPortraitBase64())
                .characterImageUrl(request.getCharacterImageUrl())
                .thumbnailBase64(request.getThumbnailBase64())
                .build();
    }

    public static CharacterResponseDTO toDTO(Character character) {
        return CharacterResponseDTO.builder()
                .id(character.getId())
                .name(character.getName())
                .role(character.getRole())
                .color(character.getColor())
                .seriesId(character.getSeries().getId())
                .characterImageBase64(character.getCharacterImageBase64())
                .portraitBase64(character.getPortraitBase64())
                .characterImageUrl(character.getCharacterImageUrl())
                .thumbnailBase64(character.getThumbnailBase64())
                .build();
    }

    /** Lightweight summary (no portrait / full image) for embedding in other DTOs. */
    public static CharacterSummaryDTO toSummaryDTO(Character character) {
        return CharacterSummaryDTO.builder()
                .id(character.getId())
                .name(character.getName())
                .role(character.getRole())
                .color(character.getColor())
                .seriesId(character.getSeries().getId())
                .thumbnailBase64(character.getThumbnailBase64())
                .characterImageUrl(character.getCharacterImageUrl())
                .build();
    }

    /**
     * Applies request fields onto an existing Character entity for update operations.
     * Only non-null values overwrite existing ones.
     */
    public static void applyUpdate(CreateCharacterRequest request, Character character) {
        if (request.getName()                != null) character.setName(request.getName());
        if (request.getRole()                != null) character.setRole(request.getRole());
        if (request.getColor()               != null) character.setColor(request.getColor());
        if (request.getCharacterImageBase64()!= null) character.setCharacterImageBase64(request.getCharacterImageBase64());
        if (request.getPortraitBase64()      != null) character.setPortraitBase64(request.getPortraitBase64());
        if (request.getCharacterImageUrl()   != null) character.setCharacterImageUrl(request.getCharacterImageUrl());
        if (request.getThumbnailBase64()     != null) character.setThumbnailBase64(request.getThumbnailBase64());
    }
}