package com.example.demospring1.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AuthorDto {

    private Long id;

    @NotBlank(message = "Author name cannot be blank")
    private String name;

    private List<String> books;

}