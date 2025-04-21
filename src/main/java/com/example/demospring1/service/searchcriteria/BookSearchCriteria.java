package com.example.demospring1.service.searchcriteria;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookSearchCriteria {
    private String bookId;
    private String title;
    private String description;
    private String series;
    private Integer minPages;
    private Integer maxPages;
    private Float minPrice;
    private Float maxPrice;
    private String language;
    private String edition;
    private String bookFormat;
    private String isbn;
    private Float rating;
    private Integer minNumRatings;
    private Integer maxNumRatings;
    private Integer minLikedPercent;
    private Integer maxLikedPercent;
    private Float minRating;
    private Float maxRating;
    private String author;
    private String genre;
    private String publisher;
    private String award;
    private String setting;
    private String character;
    private Integer minBbeVotes;
    private Integer maxBbeVotes;
    private Integer minBbeScore;
    private Integer maxBbeScore;
    private LocalDate publishDate;
    private LocalDate publishDateStart;
    private LocalDate publishDateEnd;
    private LocalDate firstPublishDate;
    private LocalDate firstPublishDateStart;
    private LocalDate firstPublishDateEnd;
}