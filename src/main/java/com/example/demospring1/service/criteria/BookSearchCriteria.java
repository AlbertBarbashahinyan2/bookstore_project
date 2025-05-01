package com.example.demospring1.service.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

@Getter
@Setter
public class BookSearchCriteria extends SearchCriteria {
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

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();

        return pageRequest.withSort(
                Sort.by("title").descending()
        );
    }
}