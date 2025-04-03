package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query(""" 
            SELECT g.name
            FROM Genre g
            """)
    List<String> findAllGenreNames();

    Genre findByName(String name);
}
