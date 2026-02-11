package com.tokubase.service;

import com.tokubase.dto.character.CreateCharacterRequest;
import com.tokubase.dto.character.CharacterResponseDTO;

import java.util.List;

public interface CharacterService {

    CharacterResponseDTO createCharacter(CreateCharacterRequest request);

    CharacterResponseDTO getCharacterById(Long id);

    List<CharacterResponseDTO> getCharactersBySeries(Long seriesId);

    void deleteCharacter(Long id);
}

