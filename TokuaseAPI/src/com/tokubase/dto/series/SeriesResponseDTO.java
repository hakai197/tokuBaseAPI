package com.tokubase.dto.series;

import com.tokubase.model.SeriesType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeriesResponseDTO {

    private Long id;
    private String name;
    private SeriesType type;
    private Integer yearStart;
    private Integer yearEnd;
    private String description;

    // Image fields
    private String logoBase64;
    private String posterBase64;
    private String bannerBase64;
    private String seriesImageBase64;
    private String thumbnailBase64;
    private String seriesImageUrl;
}