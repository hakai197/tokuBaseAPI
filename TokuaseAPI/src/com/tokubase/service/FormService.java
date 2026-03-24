package com.tokubase.service;

import com.tokubase.dto.form.CreateFormRequest;
import com.tokubase.dto.form.FormResponseDTO;

import java.util.List;

public interface FormService {

    FormResponseDTO createForm(CreateFormRequest request);

    FormResponseDTO getFormById(Long id);

    List<FormResponseDTO> getFormsByCharacter(Long characterId);

    List<FormResponseDTO> getFinalFormsByCharacter(Long characterId);

    FormResponseDTO updateForm(Long id, CreateFormRequest request);

    void deleteForm(Long id);
}