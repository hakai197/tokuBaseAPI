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
}
