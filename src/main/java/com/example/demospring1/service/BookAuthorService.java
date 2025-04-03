package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookAuthor;
import com.example.demospring1.persistence.repository.BookAuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookAuthorService {
    private final BookAuthorRepository bookAuthorRepository;

    public void save(BookAuthor bookAuthor) {

        bookAuthorRepository.save(bookAuthor);
    }

    public void saveAll(List<BookAuthor> bookAuthors) {
        bookAuthorRepository.saveAll(bookAuthors);
    }
}
