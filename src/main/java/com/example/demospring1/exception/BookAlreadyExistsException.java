package com.example.demospring1.exception;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String bookId) {
        super("Book already exists with id: " + bookId);
    }
}
