package com.example.demospring1.service.imagehandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Component
public class ImageDownloader {

    public File download(String urlStr, Path target) {
        log.info("Downloading image from URL: {}", urlStr);
        if (Files.exists(target)) {
            log.info("Image already exists at: {}", target);
            return target.toFile();
        }
        try (InputStream in = new URL(urlStr).openStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Image downloaded to: {}", target);
            return target.toFile();
        } catch (IOException e) {
            log.warn("Failed to download image from {}: {}", urlStr, e.getMessage());
            return null;
        }
    }
}
