package com.tokubase.dto.series;

import com.tokubase.dto.character.CharacterSummaryDTO;
import com.tokubase.dto.episode.EpisodeResponseDTO;
import com.tokubase.model.SeriesType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Full series detail including character and episode lists.
 * Returned by GET /api/series/{id}/detail
 */
@Getter
@Builder
public class SeriesDetailDTO {

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

    private List<CharacterSummaryDTO> characters;
    private List<EpisodeResponseDTO>  episodes;
}
