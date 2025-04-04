package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.BookAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAwardRepository extends JpaRepository<BookAward, Long> {
}
