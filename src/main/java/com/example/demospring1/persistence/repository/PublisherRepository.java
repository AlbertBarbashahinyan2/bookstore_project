package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Query(""" 
            SELECT p.name
            FROM Publisher p
            """)
    List<String> findAllPublisherNames();

    Publisher findByName(String name);
}
