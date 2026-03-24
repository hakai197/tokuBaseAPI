package com.tokubase.service;

import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.dto.character.CharacterSummaryDTO;
import com.tokubase.dto.character.CreateCharacterRequest;
import com.tokubase.exception.DuplicateResourceException;
import com.tokubase.exception.ResourceNotFoundException;
import com.tokubase.mapper.CharacterMapper;
import com.tokubase.model.Character;
import com.tokubase.model.CharacterRole;
import com.tokubase.model.Series;
import com.tokubase.repository.CharacterRepository;
import com.tokubase.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final SeriesRepository    seriesRepository;

    // ── Read ─────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<CharacterResponseDTO> getAllCharacters() {
        return characterRepository.findAll().stream()
                .map(CharacterMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterResponseDTO getCharacterById(Long id) {
        return CharacterMapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharacterResponseDTO> getCharactersBySeries(Long seriesId) {
        verifySeriesExists(seriesId);
        return characterRepository.findBySeriesId(seriesId).stream()
                .map(CharacterMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharacterSummaryDTO> getCharacterSummariesBySeries(Long seriesId) {
        verifySeriesExists(seriesId);
        return characterRepository.findBySeriesId(seriesId).stream()
                .map(CharacterMapper::toSummaryDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharacterResponseDTO> getCharactersByRole(CharacterRole role) {
        return characterRepository.findByRole(role).stream()
                .map(CharacterMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CharacterResponseDTO> getCharactersBySeriesAndRole(Long seriesId, CharacterRole role) {
        verifySeriesExists(seriesId);
        return characterRepository.findBySeriesIdAndRole(seriesId, role).stream()
                .map(CharacterMapper::toDTO)
                .toList();
    }

    // ── Write ─────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public CharacterResponseDTO createCharacter(CreateCharacterRequest request) {
        Series series = seriesRepository.findById(request.getSeriesId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Series not found with id: " + request.getSeriesId()));

        if (characterRepository.existsByNameAndSeriesId(request.getName(), request.getSeriesId())) {
            throw new DuplicateResourceException(
                    "Character '" + request.getName() + "' already exists in this series");
        }

        Character character = CharacterMapper.toEntity(request);
        character.setSeries(series);

        return CharacterMapper.toDTO(characterRepository.save(character));
    }

    @Override
    @Transactional
    public CharacterResponseDTO updateCharacter(Long id, CreateCharacterRequest request) {
        Character character = findOrThrow(id);

        // If the series is changing, validate the new series exists
        if (request.getSeriesId() != null
                && !request.getSeriesId().equals(character.getSeries().getId())) {
            Series newSeries = seriesRepository.findById(request.getSeriesId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Series not found with id: " + request.getSeriesId()));
            character.setSeries(newSeries);
        }

        CharacterMapper.applyUpdate(request, character);
        return CharacterMapper.toDTO(characterRepository.save(character));
    }

    @Override
    @Transactional
    public void deleteCharacter(Long id) {
        if (!characterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Character not found with id: " + id);
        }
        characterRepository.deleteById(id);
    }

    // ── Search ────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public Page<CharacterResponseDTO> searchCharacters(String name, CharacterRole role,
                                                        String color, Long seriesId,
                                                        Pageable pageable) {
        return characterRepository.search(name, role, color, seriesId, pageable)
                .map(CharacterMapper::toDTO);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private Character findOrThrow(Long id) {
        return characterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Character not found with id: " + id));
    }

    private void verifySeriesExists(Long seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ResourceNotFoundException("Series not found with id: " + seriesId);
        }
    }
}
