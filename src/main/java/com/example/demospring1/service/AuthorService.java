package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Author;
import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookAuthor;
import com.example.demospring1.persistence.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookAuthorService bookAuthorService;

    public void save(Author author) {
        authorRepository.save(author);
    }

    public Author findByName(String name) {
        return authorRepository.findByName(name);
    }

    public List<String> getAllAuthorNames() {
        return authorRepository.findAllAuthorNames();
    }

    public void saveAll(List<Author> authors) {
        authorRepository.saveAll(authors);
    }

    void processAuthorsAndBookAuthors(String[] authorNames, Map<String, Author> processedAuthors,
                                      List<Author> authors, Book book, List<BookAuthor> bookAuthors) {
        for (String name : authorNames) {
            name = name.trim();
            if (name.isBlank()) {
                LOGGER.warning("Empty or blank author name found, skipping.");
                continue;
            }

            Author author;
            if (processedAuthors.containsKey(name)) {
                author = processedAuthors.get(name);
            } else {
                // Create a new transient Author and cache it
                author = new Author();
                author.setName(name);
                authors.add(author);
                processedAuthors.put(name, author);
            }

            bookAuthorService.setupBookAuthors(book, bookAuthors, author);

        }
    }

}