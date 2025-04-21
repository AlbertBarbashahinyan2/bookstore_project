package com.example.demospring1.service.imagehandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
public class ImageValidator {

    public boolean isValid(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();

            return connection.getResponseCode() == HttpURLConnection.HTTP_OK
                    && connection.getContentType().startsWith("image/");
        } catch (Exception e) {
            return false;
        }
    }

}