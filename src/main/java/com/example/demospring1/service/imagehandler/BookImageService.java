package com.example.demospring1.service.imagehandler;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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
    private final BookRepository bookRepository;


    public void processImage(String imageUrl, Path[] paths) {
        log.info("Processing async image {} in thread: {}", paths[0], Thread.currentThread().getName());
        File downloaded = imageDownloader.download(imageUrl, paths[0]);
        imageResizer.createThumbnail(downloaded, paths[1]);

    }

    public Path[] getImagePath(String bookId, String imageUrl) {
        boolean isValid = imageValidator.isValid(imageUrl);
        if (!isValid) {
            return null;
        }
        Path[] paths = new Path[2];
        Path bookDir = rootDir.resolve(bookId);
        Path original = bookDir.resolve("original.jpg");
        Path thumbnail = bookDir.resolve("thumbnail.jpg");
        paths[0] = original;
        paths[1] = thumbnail;

        try {
            Files.createDirectories(bookDir);
            return paths;
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


    @Async ("taskExecutor")
    public void processBookImageAsync(Book book) {
        String imageUrl = book.getCoverImagePath();
        String bookId = book.getBookId();
        Path[] paths = getImagePath(bookId, imageUrl);
        if (paths != null) {
            processImage(imageUrl, paths);
            book.setCoverImagePath(paths[0].toString());
            book.setThumbnailImagePath(paths[1].toString());
            bookRepository.save(book);
        } else {
            log.warn("Setting cover image path to null for book: {}", bookId);
        }
    }
}