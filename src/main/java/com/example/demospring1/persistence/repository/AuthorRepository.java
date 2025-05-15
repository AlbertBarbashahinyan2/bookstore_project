package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, JpaSpecificationExecutor<Author> {

    @Query(""" 
            SELECT a.name
            FROM Author a
            """)
    List<String> findAllAuthorNames();

    Author findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String authorName);
}