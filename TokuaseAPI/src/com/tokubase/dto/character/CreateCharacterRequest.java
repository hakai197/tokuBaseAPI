package com.tokubase.dto.character;

import com.tokubase.model.CharacterRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCharacterRequest {

    @NotBlank
    private String name;

    @NotNull
    private CharacterRole role;

    private String color;

    @NotNull
    private Long seriesId;

    // Image fields (optional)
    private String characterImageBase64;  // Base64 encoded image
    private String portraitBase64;        // Base64 encoded portrait
    private String characterImageUrl;     // URL for cloud storage
    private String thumbnailBase64;       // Base64 encoded thumbnail
}