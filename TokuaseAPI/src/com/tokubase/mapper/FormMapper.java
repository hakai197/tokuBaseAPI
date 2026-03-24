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
                .formImageBase64(request.getFormImageBase64())
                .iconBase64(request.getIconBase64())
                .formImageUrl(request.getFormImageUrl())
                .thumbnailBase64(request.getThumbnailBase64())
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
                .formImageBase64(form.getFormImageBase64())
                .iconBase64(form.getIconBase64())
                .formImageUrl(form.getFormImageUrl())
                .thumbnailBase64(form.getThumbnailBase64())
                .build();
    }

    /** Applies request fields onto an existing Form entity for update operations. */
    public static void applyUpdate(CreateFormRequest request, Form form) {
        if (request.getName()           != null) form.setName(request.getName());
        if (request.getPowerType()      != null) form.setPowerType(request.getPowerType());
        if (request.getIsFinalForm()    != null) form.setIsFinalForm(request.getIsFinalForm());
        if (request.getFormImageBase64()!= null) form.setFormImageBase64(request.getFormImageBase64());
        if (request.getIconBase64()     != null) form.setIconBase64(request.getIconBase64());
        if (request.getFormImageUrl()   != null) form.setFormImageUrl(request.getFormImageUrl());
        if (request.getThumbnailBase64()!= null) form.setThumbnailBase64(request.getThumbnailBase64());
    }
}