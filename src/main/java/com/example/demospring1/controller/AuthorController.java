package com.example.demospring1.controller;

import com.example.demospring1.persistence.entity.Author;
import com.example.demospring1.persistence.specification.AuthorSpecification;
import com.example.demospring1.service.AuthorService;
import com.example.demospring1.service.criteria.AuthorSearchCriteria;
import com.example.demospring1.service.dto.AuthorDto;
import com.example.demospring1.service.dto.PageResponseDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;
    private final AuthorSpecification authorSpecification;

    @Autowired
    public AuthorController(AuthorService authorService, AuthorSpecification authorSpecification) {
        this.authorService = authorService;
        this.authorSpecification = authorSpecification;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CREATE_AUTHOR')")
    public ResponseEntity<String> createAuthor(@RequestBody @Valid AuthorDto authorDto) {
        try {
            authorService.createAuthorFromDto(authorDto);
            return ResponseEntity.ok("Author created successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{authorName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('DELETE_AUTHOR')")
    public ResponseEntity<String> deleteAuthor(@PathVariable String authorName) {
        try {
            authorService.deleteAuthor(authorName);
            return ResponseEntity.ok("Author deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<AuthorDto>> searchAuthors(AuthorSearchCriteria criteria) {
        Specification<Author> spec = authorSpecification.withCriteria(criteria);
        PageResponseDto<AuthorDto> results = authorService.findAll(spec, criteria);
        return ResponseEntity.ok(results);
    }

}
