package com.example.demospring1.controller;

import com.example.demospring1.service.CsvUploadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CsvUploadController {

    private final CsvUploadService csvUploadService;

    public CsvUploadController(CsvUploadService csvUploadService) {
        this.csvUploadService = csvUploadService;
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            csvUploadService.processCsv(file);
            return ResponseEntity.ok("File processing complete successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
