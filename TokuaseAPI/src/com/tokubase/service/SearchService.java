package com.tokubase.service;
import com.tokubase.dto.search.GlobalSearchResponseDTO;
public interface SearchService {
    /**
     * Global text search across series names, character names, and episode titles.
     *
     * @param query the search term (case-insensitive substring match)
     * @param limit maximum results per entity type (1-100)
     */
    GlobalSearchResponseDTO globalSearch(String query, int limit);
}
