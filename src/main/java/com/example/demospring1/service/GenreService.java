package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Author;
import com.example.demospring1.persistence.entity.Genre;
import com.example.demospring1.persistence.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public void save(Genre genre) {
        genreRepository.save(genre);
    }

    public List<String> getAllGenreNames(){ return genreRepository.findAllGenreNames(); }

    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }

    public void saveAll(List<Genre> genres) {
        genreRepository.saveAll(genres);
    }
}
