package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.BookCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCharacterRepository extends JpaRepository<BookCharacter, Long> {
}
