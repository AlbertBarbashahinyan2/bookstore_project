package com.example.demospring1.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String bookId) {
        super("Book with id " + bookId + " not found.");
    }
}
