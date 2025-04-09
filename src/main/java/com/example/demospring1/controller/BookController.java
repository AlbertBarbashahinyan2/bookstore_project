package com.example.demospring1.controller;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.service.BookService;

import com.example.demospring1.service.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createBook(@RequestBody BookDto bookDto) {
        try {
            bookService.createBookFromDto(bookDto);
            return ResponseEntity.ok("Book created successfully.");
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

}
