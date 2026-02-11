package com.tokubase.controller;

import com.tokubase.dto.character.CreateCharacterRequest;
import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.service.CharacterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @PostMapping
    public CharacterResponseDTO createCharacter(
            @Valid @RequestBody CreateCharacterRequest request) {
        return characterService.createCharacter(request);
    }

    @GetMapping("/{id}")
    public CharacterResponseDTO getCharacterById(@PathVariable Long id) {
        return characterService.getCharacterById(id);
    }

    @GetMapping("/series/{seriesId}")
    public List<CharacterResponseDTO> getCharactersBySeries(@PathVariable Long seriesId) {
        return characterService.getCharactersBySeries(seriesId);
    }

    @DeleteMapping("/{id}")
    public void deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacter(id);
    }
}