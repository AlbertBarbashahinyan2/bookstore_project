package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.Award;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwardRepository extends JpaRepository<Award, Long> {
    @Query(""" 
            SELECT a.name
            FROM Award a
            """)
    List<String> findAllAwardNames();

    Award findByName(String name);
}
