package com.tokubase.repository;

import com.tokubase.model.Character;
import com.tokubase.model.Form;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findByCharacterId(Long characterId);
    boolean existByNameAndCharacterId( String name, Long characterId);

    Collection<Object> findByCharacter(Character character);

    Collection<Object> findByCharacterAndIsFinalFormTrue(Character character);
}
