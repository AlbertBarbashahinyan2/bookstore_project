package com.example.demospring1.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rating")
@Setter
@Getter
@ToString
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "rating")
    private Float rating = 0f;

    @Column(name = "num_ratings")
    private Integer numRatings = 0;

    @Column(name = "liked_percent")
    private Integer likedPercent = 0;

    @Column(name = "five_star_ratings")
    private Integer fiveStarRatings = 0;

    @Column(name = "four_star_ratings")
    private Integer fourStarRatings = 0;

    @Column(name = "three_star_ratings")
    private Integer threeStarRatings = 0;

    @Column(name = "two_star_ratings")
    private Integer twoStarRatings = 0;

    @Column(name = "one_star_ratings")
    private Integer oneStarRatings = 0;

    @OneToOne(mappedBy = "rating")
    private Book book;

}
