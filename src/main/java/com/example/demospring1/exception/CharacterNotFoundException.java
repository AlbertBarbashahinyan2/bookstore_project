package com.example.demospring1.exception;

public class CharacterNotFoundException extends RuntimeException {
    public CharacterNotFoundException(String characterName) {
        super("Character with name " + characterName + " not found.");
    }}
