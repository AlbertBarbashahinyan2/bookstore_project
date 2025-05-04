package com.example.demospring1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyUsedException extends RuntimeException {

    public ResourceAlreadyUsedException(String message) {
        super(message);
    }
}
