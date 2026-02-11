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
}
