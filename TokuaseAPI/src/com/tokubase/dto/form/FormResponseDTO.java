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
}