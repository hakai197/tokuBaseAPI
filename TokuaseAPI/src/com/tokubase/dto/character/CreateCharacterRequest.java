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
}
