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

    @Column(name = "book_id", nullable = false, unique = true)
    private String bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "series")
    private String series;

    @Column(name = "pages")
    private Integer pages;

    @Column(name = "description", length = 2500)
    private String description;

    @OneToMany(mappedBy = "book")
    private List<BookAuthor> authors;

    @Column(name = "price")
    private Float price;

}
