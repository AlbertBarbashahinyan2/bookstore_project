package com.example.demospring1.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String authorName) {

        super("Author with name " + authorName + " not found.");
    }
}
