package com.example.demospring1.exception;

public class AuthorAlreadyExistsException extends RuntimeException {
    public AuthorAlreadyExistsException(String name) {
        super("Author already exists with name: " + name);
    }
}
