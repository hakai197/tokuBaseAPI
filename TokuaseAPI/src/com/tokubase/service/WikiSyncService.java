package com.tokubase.service;

import com.tokubase.dto.wiki.WikiSyncResultDTO;

public interface WikiSyncService {

    /**
     * Imports all Kamen Rider series from kamenrider.fandom.com.
     * Creates new Series records; skips duplicates; refreshes image URLs for existing ones.
     */
    WikiSyncResultDTO syncRiderSeries();

    /**
     * Imports all Super Sentai series from powerrangers.fandom.com.
     */
    WikiSyncResultDTO syncSentaiSeries();

    /**
     * Runs both syncRiderSeries() and syncSentaiSeries() sequentially.
     */
    WikiSyncResultDTO syncAll();

    /**
     * Imports characters for a specific series that already exists in the database.
     * Queries the "{SeriesTitle}_characters" category on the appropriate wiki.
     *
     * @param seriesId local database ID of the series
     */
    WikiSyncResultDTO syncCharactersForSeries(Long seriesId);
}

