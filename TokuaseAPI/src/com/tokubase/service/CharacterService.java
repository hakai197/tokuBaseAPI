package com.tokubase.service;

import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.dto.character.CharacterSummaryDTO;
import com.tokubase.dto.character.CreateCharacterRequest;
import com.tokubase.model.CharacterRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CharacterService {

    // ── Read ─────────────────────────────────────────────────────────────

    List<CharacterResponseDTO> getAllCharacters();

    CharacterResponseDTO getCharacterById(Long id);

    List<CharacterResponseDTO> getCharactersBySeries(Long seriesId);

    List<CharacterSummaryDTO> getCharacterSummariesBySeries(Long seriesId);

    List<CharacterResponseDTO> getCharactersByRole(CharacterRole role);

    List<CharacterResponseDTO> getCharactersBySeriesAndRole(Long seriesId, CharacterRole role);

    // ── Write ─────────────────────────────────────────────────────────────

    CharacterResponseDTO createCharacter(CreateCharacterRequest request);

    CharacterResponseDTO updateCharacter(Long id, CreateCharacterRequest request);

    void deleteCharacter(Long id);

    // ── Search ────────────────────────────────────────────────────────────

    Page<CharacterResponseDTO> searchCharacters(String name, CharacterRole role,
                                                 String color, Long seriesId,
                                                 Pageable pageable);
}
