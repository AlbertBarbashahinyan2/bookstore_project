package com.example.demospring1.controller;

import com.example.demospring1.service.CharacterService;
import com.example.demospring1.service.dto.BookDto;
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
    public List<BookDto> getBooksWithCharacter(@PathVariable String characterName) {
        return characterService.getBooksByCharacterName(characterName);
    }
}
