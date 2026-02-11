package com.tokubase.dto.series;

import com.tokubase.model.SeriesType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSeriesRequest {

    @NotBlank
    private String name;

    @NotNull
    private SeriesType type;

    @NotNull
    @Positive
    private Integer yearStart;

    private Integer yearEnd;

    private String description;

    // Image fields (optional)
    private String logoBase64;            // Base64 encoded logo
    private String posterBase64;          // Base64 encoded poster
    private String bannerBase64;          // Base64 encoded banner
    private String seriesImageBase64;     // Base64 encoded main image
    private String thumbnailBase64;       // Base64 encoded thumbnail
    private String seriesImageUrl;        // URL for cloud storage
}