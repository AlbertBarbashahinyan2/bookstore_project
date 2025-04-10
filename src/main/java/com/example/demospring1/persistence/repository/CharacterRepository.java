package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    @Query(""" 
            SELECT c.name
            FROM Character c
            """)
    List<String> findAllCharacterNames();

    Character findByName(String name);
}
