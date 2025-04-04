package com.example.demospring1.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ratings")
@Setter
@Getter
@ToString
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "num_ratings")
    private Integer numRatings;

    @Column(name = "liked_percent")
    private Integer likedPercent;

    @Column(name = "five_star_ratings")
    private Integer fiveStarRatings;

    @Column(name = "four_star_ratings")
    private Integer fourStarRatings;

    @Column(name = "three_star_ratings")
    private Integer threeStarRatings;

    @Column(name = "two_star_ratings")
    private Integer twoStarRatings;

    @Column(name = "one_star_ratings")
    private Integer oneStarRatings;

    @OneToOne(mappedBy = "rating")
    private Book book;

}
