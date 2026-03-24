package com.tokubase.repository;

import com.tokubase.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {

    List<Form> findByCharacterId(Long characterId);

    /** All final forms for a character. */
    List<Form> findByCharacterIdAndIsFinalFormTrue(Long characterId);

    /** All final forms across all characters. */
    List<Form> findByIsFinalFormTrue();

    boolean existsByNameAndCharacterId(String name, Long characterId);
}
