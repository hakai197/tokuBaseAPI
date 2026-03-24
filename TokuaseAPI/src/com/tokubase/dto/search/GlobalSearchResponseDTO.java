package com.tokubase.dto.search;
import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.dto.episode.EpisodeResponseDTO;
import com.tokubase.dto.series.SeriesResponseDTO;
import lombok.Builder;
import lombok.Getter;
import java.util.List;
/**
 * Unified response returned by GET /api/search.
 * Each list contains matching records for its entity type.
 * Empty lists (never null) are returned when there are no matches.
 */
@Getter
@Builder
public class GlobalSearchResponseDTO {
    private String query;
    private int totalSeries;
    private int totalCharacters;
    private int totalEpisodes;
    private List<SeriesResponseDTO>    series;
    private List<CharacterResponseDTO> characters;
    private List<EpisodeResponseDTO>   episodes;
}
