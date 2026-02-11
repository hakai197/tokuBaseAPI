package com.tokubase.service;

import com.tokubase.dto.character.CreateCharacterRequest;
import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.exception.ResourceNotFoundException;
import com.tokubase.mapper.CharacterMapper;
import com.tokubase.model.Character;
import com.tokubase.model.Series;
import com.tokubase.repository.CharacterRepository;
import com.tokubase.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final SeriesRepository seriesRepository;

    @Override
    public CharacterResponseDTO createCharacter(CreateCharacterRequest request) {

        Series series = seriesRepository.findById(request.getSeriesId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Series not found with id: " + request.getSeriesId()));

        Character character = CharacterMapper.toEntity(request);
        character.setSeries(series);

        Character saved = characterRepository.save(character);

        return CharacterMapper.toDTO(saved);
    }
}
