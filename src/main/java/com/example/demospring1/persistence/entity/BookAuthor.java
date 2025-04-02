package com.example.demospring1.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "book_author"//,
//        indexes = {
//        @Index(name = "idx_bookauthor_book", columnList = "book_id"),
//        @Index(name = "idx_bookauthor_author", columnList = "author_id"),
//        @Index(name = "idx_bookauthor_composite", columnList = "book_id,author_id", unique = true)}
)
@Setter
@Getter
@ToString
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;
}
