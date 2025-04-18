package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.BookCharacter;
import com.example.demospring1.persistence.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCharacterRepository extends JpaRepository<BookCharacter, Long> {

    List<BookCharacter> findAllByCharacter(Character character);
}
