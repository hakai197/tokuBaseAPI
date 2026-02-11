package com.tokubase.dto.episode;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class EpisodeResponseDTO {
    private Long id;
    private Integer episodeNumber;
    private String title;
    private LocalDate airDate;
    private Long seriesId;
    private String seriesName;

    // Image fields
    private String imageBase64;
    private String thumbnailBase64;
    private String imageUrl;
}