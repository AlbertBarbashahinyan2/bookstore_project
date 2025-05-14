package com.example.demospring1.controller;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.specification.BookSpecification;
import com.example.demospring1.service.BookService;
import com.example.demospring1.service.CharacterService;
import com.example.demospring1.service.criteria.BookSearchCriteria;
import com.example.demospring1.service.dto.BookDto;
import com.example.demospring1.service.dto.PageResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final CharacterService characterService;
    private final BookSpecification bookSpecification;

    @Autowired
    public BookController(BookService bookService, CharacterService characterService, BookSpecification bookSpecification) {
        this.bookService = bookService;
        this.characterService = characterService;
        this.bookSpecification = bookSpecification;
    }

    @GetMapping("/{bookId}")
    public BookDto getBook(@PathVariable String bookId) {
        return bookService.getBookDto(bookId);
    }

    @GetMapping("/{bookId}/cover")
    public ResponseEntity<byte[]> getBookCover(@PathVariable String bookId) {
        byte[] coverImage = bookService.getBookCover(bookId);
        if (coverImage != null) {
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(coverImage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> createBook(@RequestBody BookDto bookDto) {
        try {
            bookService.createBookFromDto(bookDto);
            return ResponseEntity.ok("Book created successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable String bookId) {
        try {
            bookService.deleteBook(bookId);
            return ResponseEntity.ok("Book deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/rate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> rateBook(@RequestParam int star,
                                           @RequestParam String bookId) {
        try {
            bookService.addRatingToBook(star, bookId);
            return ResponseEntity.ok("Book rated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{characterName}")
    public List<BookDto> getBooksWithCharacter( @PathVariable String characterName) {
        return characterService.getBooksByCharacterName(characterName);
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<BookDto>> searchBooks(BookSearchCriteria criteria) {
        Specification<Book> spec = bookSpecification.withCriteria(criteria);
        PageResponseDto<BookDto> results = bookService.findAll(spec, criteria);
        return ResponseEntity.ok(results);
    }
//
//    @GetMapping
//    public PageResponseDto<BookDto> getAll(BookSearchCriteria criteria) {
//        return bookService.getAll(criteria);
//    }
}