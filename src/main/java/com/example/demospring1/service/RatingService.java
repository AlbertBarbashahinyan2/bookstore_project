package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.Rating;
import com.example.demospring1.persistence.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public void saveAll(List<Rating> ratings) {
        ratingRepository.saveAll(ratings);
    }

    void setupRatings(String rating, String numRatings, String likedPercent,
                      String[] ratingsByStars, Book book) {
        Rating ratingObj = new Rating();

        if (rating == null || rating.isBlank()) {
            ratingObj.setRating(null);
        } else {
            float ratingNumerical = Float.parseFloat(rating);
            if (ratingNumerical >= 0 && ratingNumerical <= 5) {
                ratingObj.setRating(ratingNumerical);
            } else {
                throw new IllegalArgumentException("Invalid rating data");
//                ratingObj.setRating(null);
            }
        }

        if (numRatings == null || numRatings.isBlank()) {
            ratingObj.setNumRatings(null);
        } else {
            int numRatingsNumerical = Integer.parseInt(numRatings);
            ratingObj.setNumRatings(numRatingsNumerical);
        }


        if (likedPercent == null || likedPercent.isBlank()) {
            ratingObj.setLikedPercent(null);
        } else {
            int likedPercentNumerical = Integer.parseInt(likedPercent);
            if (likedPercentNumerical >= 0 && likedPercentNumerical <= 100) {
                ratingObj.setLikedPercent(likedPercentNumerical);
            } else {
                throw new IllegalArgumentException("Invalid liked percent data");
//                ratingObj.setLikedPercent(null);
            }
        }


        if (ratingsByStars != null && ratingsByStars.length != 0) {
            if (ratingsByStars.length != 5) {
                throw new IllegalArgumentException("Invalid ratings by stars data");
            }
            for (int i = 0; i < ratingsByStars.length; i++) {
                String ratingsByStar = ratingsByStars[i];
                ratingsByStar = ratingsByStar.trim().replaceAll("[^\\d]", "");
                int ratingsByStarNumerical = 0;
                if (!ratingsByStar.isBlank()) {
                    ratingsByStarNumerical = Integer.parseInt(ratingsByStar);
                }
                if (i == 0)
                    ratingObj.setFiveStarRatings(ratingsByStarNumerical);
                if (i == 1)
                    ratingObj.setFourStarRatings(ratingsByStarNumerical);
                if (i == 2)
                    ratingObj.setThreeStarRatings(ratingsByStarNumerical);
                if (i == 3)
                    ratingObj.setTwoStarRatings(ratingsByStarNumerical);
                if (i == 4)
                    ratingObj.setOneStarRatings(ratingsByStarNumerical);
            }
        }

        ratingObj.setBook(book);
        book.setRating(ratingObj);
    }
}
