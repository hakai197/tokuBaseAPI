package com.tokubase.service;

import com.tokubase.dto.form.CreateFormRequest;
import com.tokubase.dto.form.FormResponseDTO;
import com.tokubase.exception.DuplicateResourceException;
import com.tokubase.exception.ResourceNotFoundException;
import com.tokubase.mapper.FormMapper;
import com.tokubase.model.Character;
import com.tokubase.model.Form;
import com.tokubase.repository.CharacterRepository;
import com.tokubase.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormRepository      formRepository;
    private final CharacterRepository characterRepository;

    @Override
    @Transactional
    public FormResponseDTO createForm(CreateFormRequest request) {
        Character character = findCharacterOrThrow(request.getCharacterId());

        if (formRepository.existsByNameAndCharacterId(request.getName(), request.getCharacterId())) {
            throw new DuplicateResourceException(
                    "Form '" + request.getName() + "' already exists for this character");
        }

        Form saved = formRepository.save(FormMapper.toEntity(request, character));
        return FormMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public FormResponseDTO getFormById(Long id) {
        return FormMapper.toDTO(findFormOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormResponseDTO> getFormsByCharacter(Long characterId) {
        findCharacterOrThrow(characterId);
        return formRepository.findByCharacterId(characterId).stream()
                .map(FormMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormResponseDTO> getFinalFormsByCharacter(Long characterId) {
        findCharacterOrThrow(characterId);
        return formRepository.findByCharacterIdAndIsFinalFormTrue(characterId).stream()
                .map(FormMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public FormResponseDTO updateForm(Long id, CreateFormRequest request) {
        Form form = findFormOrThrow(id);

        // If character is changing, validate the new character exists
        if (request.getCharacterId() != null
                && !request.getCharacterId().equals(form.getCharacter().getId())) {
            Character newCharacter = findCharacterOrThrow(request.getCharacterId());
            form.setCharacter(newCharacter);
        }

        FormMapper.applyUpdate(request, form);
        return FormMapper.toDTO(formRepository.save(form));
    }

    @Override
    @Transactional
    public void deleteForm(Long id) {
        if (!formRepository.existsById(id)) {
            throw new ResourceNotFoundException("Form not found with id: " + id);
        }
        formRepository.deleteById(id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private Form findFormOrThrow(Long id) {
        return formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + id));
    }

    private Character findCharacterOrThrow(Long characterId) {
        return characterRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Character not found with id: " + characterId));
    }
}