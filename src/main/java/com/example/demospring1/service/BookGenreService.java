package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Genre;
import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookGenre;
import com.example.demospring1.persistence.repository.BookGenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookGenreService {

    private final BookGenreRepository bookGenreRepository;

    public void save(BookGenre bookGenre) {
        bookGenreRepository.save(bookGenre);
    }

    public void saveAll(List<BookGenre> bookGenre) {
        bookGenreRepository.saveAll(bookGenre);
    }

    void setupBookGenres(Book book, List<BookGenre> bookGenres, Genre genre) {
        // Create the BookGenre relationship
        BookGenre bookGenre = new BookGenre();
        bookGenre.setBook(book);
        bookGenre.setGenre(genre);
        bookGenres.add(bookGenre);
    }
}
