package com.tokubase.service;

import com.tokubase.dto.form.CreateFormRequest;
import com.tokubase.dto.form.FormResponseDTO;
import com.tokubase.exception.ResourceNotFoundException;
import com.tokubase.mapper.FormMapper;
import com.tokubase.model.Character;
import com.tokubase.model.Form;
import com.tokubase.repository.CharacterRepository;
import com.tokubase.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;
    private final CharacterRepository characterRepository;

    @Override
    public FormResponseDTO createForm(CreateFormRequest request) {
        Character character = characterRepository.findById(request.getCharacterId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Character not found with id: " + request.getCharacterId()));

        Form form = FormMapper.toEntity(request, character);
        Form saved = formRepository.save(form);

        return FormMapper.toDTO(saved);
    }

    @Override
    public FormResponseDTO getFormById(Long id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Form not found with id: " + id));
        return FormMapper.toDTO(form);
    }

    @Override
    public List<FormResponseDTO> getFormsByCharacter(Long characterId) {
        // Verify character exists
        characterRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Character not found with id: " + characterId));

        return formRepository.findByCharacterId(characterId).stream()
                .map(form -> (Form) form)
                .map(FormMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FormResponseDTO> getFinalFormsByCharacter(Long characterId) {
        // Verify character exists
        characterRepository.findById(characterId)
                .orElseThrow(() -> new ResourceNotFoundException("Character not found with id: " + characterId));

        return formRepository.findByCharacterId(characterId).stream()
                .map(form -> (Form) form)
                .filter(form -> form.getIsFinalForm())
                .map(FormMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteForm(Long id) {
        if (!formRepository.existsById(id)) {
            throw new ResourceNotFoundException("Form not found with id: " + id);
        }
        formRepository.deleteById(id);
    }
}