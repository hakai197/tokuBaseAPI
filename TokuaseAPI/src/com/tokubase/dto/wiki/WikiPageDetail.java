package com.tokubase.dto.wiki;
import lombok.Builder;
import lombok.Getter;
/**
 * All wiki data fetched for a single page (series or character).
 * Populated by WikiApiClient from the MediaWiki API.
 */
@Getter
@Builder
public class WikiPageDetail {
    /** MediaWiki page ID */
    private Long pageId;
    /** Raw page title as returned by the API (e.g. "Kamen Rider Kuuga") */
    private String title;
    /** Plain-text introductory extract – used as the series/character description */
    private String extract;
    /** URL of the page's main thumbnail image (served from Fandom CDN) */
    private String thumbnailUrl;
    /** Full-size image URL (may be null if no page image is set) */
    private String imageUrl;
    /**
     * Best-guess broadcast start year parsed from the extract text.
     * 0 if no year could be detected.
     */
    private int yearStart;
    /**
     * Best-guess broadcast end year parsed from the extract text.
     * 0 if the series is ongoing or no end year could be detected.
     */
    private int yearEnd;
    /** Direct URL to the wiki page (for attribution / source reference) */
    private String wikiUrl;
}
