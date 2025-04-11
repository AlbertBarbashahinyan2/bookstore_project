package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.*;
import com.example.demospring1.persistence.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final BookGenreService bookGenreService;

    public void save(Genre genre) {
        genreRepository.save(genre);
    }

    public List<String> getAllGenreNames() {
        return genreRepository.findAllGenreNames();
    }

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }

    public void saveAll(List<Genre> genres) {
        genreRepository.saveAll(genres);
    }

    void processGenresAndBookGenres(String[] genreNames, Map<String, Genre> processedGenres,
                       Book book, List<BookGenre> bookGenres, List<Genre> genres) {
        for (String name : genreNames) {
            name = name.trim();
            if (name.isBlank()) {
                LOGGER.warning("Empty or blank genre name found, skipping.");
                continue;
            }
            Genre genre;
            if (processedGenres.containsKey(name)) {
                genre = processedGenres.get(name);
            } else {
                // Create a new transient Genre and cache it
                genre = new Genre();
                genre.setName(name);
                processedGenres.put(name, genre);
                genres.add(genre);
            }
            bookGenreService.setupBookGenres(book, bookGenres, genre);
        }

    }
}
