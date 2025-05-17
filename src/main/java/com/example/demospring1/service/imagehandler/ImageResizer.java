package com.example.demospring1.service.imagehandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
public class ImageResizer {

    public void createThumbnail(File original, Path thumbnailName) {
        log.info("Creating thumbnail...");
        try {
            if (Files.exists(thumbnailName)) {
                log.info("Thumbnail already exists!");
                return;
            }
            BufferedImage img = ImageIO.read(original);
            BufferedImage resized = resize(img, 150, 220); // small size

            File thumbnail = new File(original.getParent(), "thumbnail.jpg");
            ImageIO.write(resized, "jpg", thumbnail);
            log.info("Thumbnail created!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage resize(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }
}
