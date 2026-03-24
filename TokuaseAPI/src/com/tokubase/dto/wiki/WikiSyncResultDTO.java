package com.tokubase.dto.wiki;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Returned by every wiki-sync endpoint to report what was imported.
 */
@Getter
@Builder
public class WikiSyncResultDTO {
    private LocalDateTime syncedAt;
    /** "RIDER" or "SENTAI" */
    private String type;
    private int seriesCreated;
    private int seriesSkipped;   // already existed in DB
    private int seriesUpdated;   // image / description refreshed
    private int charactersCreated;
    private int charactersSkipped;
    /** Titles that were found on the wiki but could not be imported (parse errors, etc.) */
    private List<String> warnings;
    private String message;
}
