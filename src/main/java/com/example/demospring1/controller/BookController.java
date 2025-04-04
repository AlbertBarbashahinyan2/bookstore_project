package com.example.demospring1.controller;

import com.example.demospring1.service.CsvUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {
    private final CsvUploadService csvUploadService;

    @Autowired
    public BookController(CsvUploadService csvUploadService) {
        this.csvUploadService = csvUploadService;
    }

    @GetMapping
    public String test() {

        return "test";
    }

}
