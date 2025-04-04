package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.*;
import com.example.demospring1.persistence.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.demospring1.service.BookGenreService.setupBookGenres;
import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public void save(Genre genre) {
        genreRepository.save(genre);
    }

    public List<String> getAllGenreNames() {
        return genreRepository.findAllGenreNames();
    }

    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }

    public void saveAll(List<Genre> genres) {
        genreRepository.saveAll(genres);
    }


    void processGenresAndBookGenres(String[] genreNames, Map<String, Genre> processedGenres,
                                    Set<String> existingGenreNames, List<Genre> genres, Book book,
                                    List<BookGenre> bookGenres) {
        for (String name : genreNames) {
            name = name.trim();
            if (name.isBlank()) {
                LOGGER.warning("Empty or blank genre name found, skipping.");
                continue;
            }

            // Check if genre is already in processedGenres
            Genre genre = processedGenres.get(name);
            if (genre == null) {
                if (existingGenreNames.contains(name)) {
                    // Genre exists in database, fetch and cache it
                    genre = findByName(name);
                } else {
                    // Create a new transient Genre and cache it
                    genre = new Genre();
                    genre.setName(name);
                    genres.add(genre);
                }
                processedGenres.put(name, genre); // Cache the genre for future use
            }

            setupBookGenres(book, bookGenres, genre);

        }
    }
}
