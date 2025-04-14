package com.example.demospring1.controller;

import com.example.demospring1.service.AuthorService;
import com.example.demospring1.service.dto.AuthorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/{authorName}")
    public AuthorDto getAuthor(@PathVariable String authorName) {
        return authorService.findDtoByName(authorName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createAuthor(@RequestBody AuthorDto authorDto) {
        try {
            authorService.createAuthorFromDto(authorDto);
            return ResponseEntity.ok("Author created successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{authorName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteAuthor(@PathVariable String authorName) {
        try {
            authorService.deleteAuthor(authorName);
            return ResponseEntity.ok("Author deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
