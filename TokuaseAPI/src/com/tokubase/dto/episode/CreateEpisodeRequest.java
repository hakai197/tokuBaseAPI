package com.tokubase.dto.episode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class CreateEpisodeRequest {

    @NotNull(message = "Episode number is required")
    @Positive(message = "Episode number must be positive")
    private Integer episodeNumber;

    @NotBlank(message = "Episode title is required")
    private String title;

    private LocalDate airDate;

    @NotNull(message = "Series ID is required")
    private Long seriesId;

    // Image fields (optional)
    private String imageBase64;
    private String thumbnailBase64;
    private String imageUrl;
}