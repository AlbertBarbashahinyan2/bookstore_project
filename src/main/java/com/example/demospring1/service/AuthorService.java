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

    public List<Author> findOrSaveAuthorsInBatch(String[] authorNames) {
        // Deduplicate author names locally
        Set<String> uniqueAuthorNames = new HashSet<>(Arrays.asList(authorNames));

        // Fetch existing authors from the database
        List<Author> existingAuthors = findAllByNameIn(uniqueAuthorNames);

        // Remove names that already exist in the database
        Set<String> existingAuthorNames = new HashSet<>();
        for (Author existingAuthor : existingAuthors) {
            existingAuthorNames.add(existingAuthor.getName());
        }

        uniqueAuthorNames.removeAll(existingAuthorNames);

        // Create new (transient) Author entities for names not already in the database
        List<Author> newAuthors = new ArrayList<>();
        for (String name : uniqueAuthorNames) {
            Author author = new Author();
            author.setName(name);
            newAuthors.add(author);
        }

//         Batch save new authors
        if (!newAuthors.isEmpty()) {
            saveAll(newAuthors);
        }

        // Combine existing and newly saved authors
        List<Author> allAuthors = new ArrayList<>(existingAuthors);
        allAuthors.addAll(newAuthors);

        return allAuthors;
    }

    private List<Author> findAllByNameIn(Set<String> uniqueAuthorNames) {
        return authorRepository.findAllAuthorNamesIn(uniqueAuthorNames);
    }

}
