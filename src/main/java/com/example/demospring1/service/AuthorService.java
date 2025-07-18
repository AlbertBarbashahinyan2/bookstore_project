package com.example.demospring1.service;

import com.example.demospring1.exception.ResourceAlreadyUsedException;
import com.example.demospring1.exception.ResourceNotFoundException;
import com.example.demospring1.persistence.entity.*;
import com.example.demospring1.persistence.repository.AuthorRepository;
import com.example.demospring1.service.criteria.AuthorSearchCriteria;
import com.example.demospring1.service.dto.AuthorDto;
import com.example.demospring1.service.dto.PageResponseDto;
import com.example.demospring1.service.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final BookAuthorService bookAuthorService;
    private final AuthorMapper authorMapper;
    private final BookService bookService;

    public void save(Author author) {
        authorRepository.save(author);
    }

    public AuthorDto findDtoByName(String name) {
        name = name.trim();
        if (authorRepository.findByName(name) == null) {
            throw new ResourceNotFoundException("Author with name " + name + " not found");
        }
        return authorMapper.toDto(authorRepository.findByName(name));
    }

    public Author findByName(String name) {
        name = name.trim();
        if (authorRepository.findByName(name) == null) {
            throw new ResourceNotFoundException("Author with name " + name + " not found");
        }
        return authorRepository.findByName(name);
    }

    public List<String> getAllAuthorNames() {
        return authorRepository.findAllAuthorNames();
    }

    public PageResponseDto<AuthorDto> findAll(Specification<Author> spec, AuthorSearchCriteria criteria) {
        Page<Author> authors = authorRepository.findAll(spec, criteria.buildPageRequest());
        if (authors.isEmpty()) {
            throw new ResourceNotFoundException("No authors found");
        }
        return PageResponseDto.from(authors.map(authorMapper::toDto));
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
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

    @Transactional
    public void createAuthorFromDto(AuthorDto dto) {
        if (authorRepository.existsByName(dto.getName())) {
            throw new ResourceAlreadyUsedException("Author already exists with name: " + dto.getName());
        }
        Author author = new Author();
        author.setName(dto.getName());

        if (dto.getBooks() != null) {

            List<BookAuthor> bookAuthors = new ArrayList<>();

            for (String bookId : dto.getBooks()) {
                if (bookId == null || bookId.trim().isBlank()) continue;
                String cleanBookId = bookId.trim();

                Book book = bookService.getBook(cleanBookId);
                bookAuthorService.setupBookAuthors(book, bookAuthors, author);

            }

            bookAuthorService.saveAll(bookAuthors);
            authorRepository.save(author);
        }
    }

    @Transactional
    public void deleteAuthor(String authorName) {
        if (authorRepository.findByName(authorName) == null) {
            throw new ResourceNotFoundException("Author with name " + authorName + " not found");
        }
        authorRepository.deleteByName(authorName);
    }
}