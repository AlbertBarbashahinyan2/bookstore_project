package com.example.demospring1.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class BookDto {

    private Long id;

    @NotBlank(message = "Book ID cannot be blank")
    private String bookId;

    @NotBlank(message = "Title cannot be blank")
    private String title;
    private String description;
    private String series;
    private Integer pages;
    private Float price;
    private String language;
    private String edition;
    private String bookFormat;
    private String isbn;
    private int[] ratingsByStars;
    private List<String> authors;
    private List<String> genres;
    private List<String> publishers;
    private List<String> awards;
    private List<String> settings;
    private List<String> characters;

}
