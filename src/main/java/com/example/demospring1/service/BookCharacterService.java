package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookCharacter;
import com.example.demospring1.persistence.entity.Character;
import com.example.demospring1.persistence.repository.BookCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCharacterService {

    private final BookCharacterRepository bookCharacterRepository;

    public void save(BookCharacter bookCharacter) {
        bookCharacterRepository.save(bookCharacter);
    }

    public void saveAll(List<BookCharacter> bookCharacter) {
        bookCharacterRepository.saveAll(bookCharacter);
    }

    void setupBookCharacters(Book book, List<BookCharacter> bookCharacters, Character character) {
        // Create the BookCharacter relationship
        BookCharacter bookCharacter = new BookCharacter();
        bookCharacter.setBook(book);
        bookCharacter.setCharacter(character);
        bookCharacters.add(bookCharacter);
    }

    public List<BookCharacter> getAllByCharacter(Character character) {
        return bookCharacterRepository.findAllByCharacter(character);
    }
}
