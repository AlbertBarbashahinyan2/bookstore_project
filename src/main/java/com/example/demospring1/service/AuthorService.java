package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Author;
import com.example.demospring1.persistence.entity.BookAuthor;
import com.example.demospring1.persistence.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public void save(Author author) {
        authorRepository.save(author);
    }

    public Author findByName(String name) {
        return authorRepository.findByName(name);
    }

    public List<String> getAllAuthorNames(){ return authorRepository.findAllAuthorNames(); }

    public void saveAll(List<Author> authors) {
        authorRepository.saveAll(authors);
    }

}
