package com.example.demospring1.persistence.repository;

import com.example.demospring1.persistence.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book getByBookId(String bookId);

    void deleteByBookId(String bookId);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END FROM Book b WHERE b.bookId = :bookId")
    boolean existsByBookId(String bookId);


    @Query(""" 
            SELECT b.bookId 
            FROM Book b
            """)
    List<String> findAllBookIds();

    Book findBookById(Long id);
}
