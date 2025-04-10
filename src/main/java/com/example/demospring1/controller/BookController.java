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
        return bookService.createBookFromDto(bookDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    @PostMapping("/rate")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> rateBook(@RequestParam int star,
                                           @RequestParam String bookId) {
        return bookService.addRatingToBook(star, bookId);
    }
}