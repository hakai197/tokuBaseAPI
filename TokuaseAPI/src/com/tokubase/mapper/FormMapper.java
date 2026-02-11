package com.tokubase.mapper;

import com.tokubase.dto.form.CreateFormRequest;
import com.tokubase.dto.form.FormResponseDTO;
import com.tokubase.model.Character;
import com.tokubase.model.Form;

public class FormMapper {

    private FormMapper() {}

    public static Form toEntity(CreateFormRequest request, Character character) {
        return Form.builder()
                .name(request.getName())
                .powerType(request.getPowerType())
                .isFinalForm(request.getIsFinalForm() != null ? request.getIsFinalForm() : false)
                .character(character)
                .build();
    }

    public static FormResponseDTO toDTO(Form form) {
        return FormResponseDTO.builder()
                .id(form.getId())
                .name(form.getName())
                .powerType(form.getPowerType())
                .isFinalForm(form.getIsFinalForm())
                .characterId(form.getCharacter().getId())
                .characterName(form.getCharacter().getName())
                .build();
    }
}