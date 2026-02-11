package com.tokubase.dto.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFormRequest {

    @NotBlank(message = "Form name is required")
    private String name;

    private String powerType;

    private Boolean isFinalForm = false;

    @NotNull(message = "Character ID is required")
    private Long characterId;

    // Image fields (optional)
    private String formImageBase64;       // Base64 encoded form image
    private String iconBase64;            // Base64 encoded icon
    private String formImageUrl;          // URL for cloud storage
    private String thumbnailBase64;       // Base64 encoded thumbnail
}