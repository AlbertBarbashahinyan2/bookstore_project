package com.example.demospring1.service.enums;

public enum Permission {
    // Book permissions
    VIEW_BOOK,
    CREATE_BOOK,
    DELETE_BOOK,
    UPDATE_BOOK,
    RATE_BOOK,
    VIEW_BOOK_COVER,

    // Author permissions
    VIEW_AUTHOR,
    CREATE_AUTHOR,
    DELETE_AUTHOR,
    UPDATE_AUTHOR,

    // User permissions
    VIEW_USER,
    CREATE_USER,
    UPDATE_USER,
    DELETE_USER,
    ASSIGN_ROLE,

    // CSV
    UPLOAD_CSV
}
