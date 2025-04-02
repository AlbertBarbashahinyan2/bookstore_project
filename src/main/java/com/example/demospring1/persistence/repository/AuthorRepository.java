package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(""" 
            SELECT a.name
            FROM Author a
            """)
    List<String> findAllAuthorNames();

    Author findByName(String name);

}