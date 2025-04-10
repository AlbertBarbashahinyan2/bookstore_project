package com.example.demospring1.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "book")
@Setter
@Getter
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_identifier", nullable = false, unique = true)
    private String bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "series")
    private String series;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookAuthor> authors;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookGenre> genres;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookPublisher> publishers;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookSetting> settings;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookAward> awards;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookCharacter> characters;

    @Column(name = "price")
    private Float price;

    @Column(name = "language")
    private String language;

    @Column(name = "edition")
    private String edition;

    @Column(name = "book_format")
    private String bookFormat;

    @Column(name = "isbn")
    private String isbn;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id")
    private Rating rating;

}
