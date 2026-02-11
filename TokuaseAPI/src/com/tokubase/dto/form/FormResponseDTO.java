package com.tokubase.dto.form;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FormResponseDTO {
    private Long id;
    private String name;
    private String powerType;
    private Boolean isFinalForm;
    private Long characterId;
    private String characterName;

    // Image fields
    private String formImageBase64;
    private String iconBase64;
    private String formImageUrl;
    private String thumbnailBase64;
}