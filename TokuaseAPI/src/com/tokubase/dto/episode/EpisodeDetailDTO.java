package com.tokubase.dto.episode;

import com.tokubase.model.SeriesType;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class EpisodeDetailDTO {
    private Long id;
    private Integer episodeNumber;
    private String title;
    private LocalDate airDate;
    private SeriesSummaryDTO series;

    // Image fields
    private String imageBase64;
    private String thumbnailBase64;
    private String imageUrl;

    @Getter
    @Builder
    public static class SeriesSummaryDTO {
        private Long id;
        private String name;
        private SeriesType type;
    }
}