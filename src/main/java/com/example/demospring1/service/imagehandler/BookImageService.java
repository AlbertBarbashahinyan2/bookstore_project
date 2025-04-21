package com.example.demospring1.service.imagehandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookImageService {
    private final ImageDownloader imageDownloader;
    private final ImageResizer imageResizer;
    private final ImageValidator imageValidator;
    private final Path rootDir = Paths.get("book-images");


    public void processImage(String imageUrl, Path target) {
        log.info("Processing async image {} in thread: {}", target, Thread.currentThread().getName());
            File downloaded = imageDownloader.download(imageUrl, target);
            imageResizer.createThumbnail(downloaded);

    }

    public Path getImagePath(String bookId, String imageUrl) {
        boolean isValid = imageValidator.isValid(imageUrl);
        if (!isValid) {
            return null;
        }

        Path bookDir = rootDir.resolve(bookId);
        Path target = bookDir.resolve("original.jpg");

        try {
            Files.createDirectories(bookDir);
            return target;
        } catch (IOException e) {
            log.warn("Could not create directory: {}", e.getMessage());
            return null;
        }
    }

    public byte[] getImage(Path imagePath) {
        try {
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            log.error("Error reading image file: {}", e.getMessage());
            return null;
        }
    }

}