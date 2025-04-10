package com.example.demospring1.service;

import com.example.demospring1.exception.CharacterNotFoundException;
import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookCharacter;
import com.example.demospring1.persistence.entity.Character;
import com.example.demospring1.persistence.repository.CharacterRepository;
import com.example.demospring1.service.dto.BookDto;
import com.example.demospring1.service.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final BookCharacterService bookCharacterService;
    private final BookMapper bookMapper;

    public void save(Character character) {
        characterRepository.save(character);
    }

    public List<String> getAllCharacterNames() {
        return characterRepository.findAllCharacterNames();
    }

    public Character findByName(String name) {
        return characterRepository.findByName(name);
    }

    public void saveAll(List<Character> characters) {
        characterRepository.saveAll(characters);
    }

    void processCharactersAndBookCharacters(String[] characterNames, Map<String, Character> processedCharacters,
                                    Book book, List<BookCharacter> bookCharacters, List<Character> characters) {
        for (String name : characterNames) {
            name = name.trim();
            if (name.isBlank()) {
                LOGGER.warning("Empty or blank character name found, skipping.");
                continue;
            }
            Character character;
            if (processedCharacters.containsKey(name)) {
                character = processedCharacters.get(name);
            } else {
                // Create a new transient Character and cache it
                character = new Character();
                character.setName(name);
                processedCharacters.put(name, character);
                characters.add(character);
            }
            bookCharacterService.setupBookCharacters(book, bookCharacters, character);
        }

    }

    public List<BookDto> getBooksByCharacterName(String characterName) {
        characterName = characterName.trim();
        Character character = findByName(characterName);
        if (character == null) {
            throw new CharacterNotFoundException(characterName);
        }
        List<BookCharacter> bookCharacters = bookCharacterService.getAllByCharacter(character);
        List<Book> books = new ArrayList<>();
        for (BookCharacter bookCharacter : bookCharacters) {
            books.add(bookCharacter.getBook());
        }
        return bookMapper.toDtos(books);
    }
}
