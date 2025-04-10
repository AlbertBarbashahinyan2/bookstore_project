package com.example.demospring1.controller;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.service.CharacterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/character")
public class CharacterController {
    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/{characterName}")
    public List<Book> getBooksWithCharacter(@PathVariable String characterName) {
        return characterService.getBooksByCharacterName(characterName);
    }
}
