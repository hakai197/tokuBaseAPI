package com.tokubase.mapper;

import com.tokubase.dto.form.CreateFormRequest;
import com.tokubase.dto.form.FormResponseDTO;
import com.tokubase.model.Character;
import com.tokubase.model.Form;

public class FormMapper {

    private FormMapper() {}

    public static Form toEntity(CreateFormRequest request, Character character) {
        Form.FormBuilder builder = Form.builder()
                .name(request.getName())
                .powerType(request.getPowerType())
                .isFinalForm(request.getIsFinalForm() != null ? request.getIsFinalForm() : false)
                .character(character);

        // Map image fields if present
        if (request.getFormImageBase64() != null) {
            builder.formImageBase64(request.getFormImageBase64());
        }
        if (request.getIconBase64() != null) {
            builder.iconBase64(request.getIconBase64());
        }
        if (request.getFormImageUrl() != null) {
            builder.formImageUrl(request.getFormImageUrl());
        }
        if (request.getThumbnailBase64() != null) {
            builder.thumbnailBase64(request.getThumbnailBase64());
        }

        return builder.build();
    }

    public static FormResponseDTO toDTO(Form form) {
        FormResponseDTO.FormResponseDTOBuilder builder = FormResponseDTO.builder()
                .id(form.getId())
                .name(form.getName())
                .powerType(form.getPowerType())
                .isFinalForm(form.getIsFinalForm())
                .characterId(form.getCharacter().getId())
                .characterName(form.getCharacter().getName());

        // Map image fields
        builder.formImageBase64(form.getFormImageBase64())
                .iconBase64(form.getIconBase64())
                .formImageUrl(form.getFormImageUrl())
                .thumbnailBase64(form.getThumbnailBase64());

        return builder.build();
    }
}