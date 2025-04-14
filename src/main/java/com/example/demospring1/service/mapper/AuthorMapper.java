package com.example.demospring1.service.mapper;

import com.example.demospring1.persistence.entity.Author;
import com.example.demospring1.service.dto.AuthorDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorMapper {

    public AuthorDto toDto(Author author) {
        if (author == null) {
            return null;
        }
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setBooks(mapBooks(author));
        return authorDto;
    }

    private List<String> mapBooks(Author author) {
        if (author.getBooks() == null) {
            return List.of(); // Return an empty list if books are null
        }
        return author.getBooks().stream()
                .map(bookAuthor -> bookAuthor.getBook().getBookId()).toList();
    }
}