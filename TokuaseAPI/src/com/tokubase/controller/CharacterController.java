package com.tokubase.controller;


import com.tokubase.dto.character.CharacterResponseDTO;
import com.tokubase.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping
    public List<CharacterResponseDTO>getAllCharacters() { return characterService.getAllCharacters();}
}
