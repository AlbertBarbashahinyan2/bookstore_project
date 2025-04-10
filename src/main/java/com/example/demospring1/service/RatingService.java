package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.Rating;
import com.example.demospring1.persistence.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    public void saveAll(List<Rating> ratings) {
        ratingRepository.saveAll(ratings);
    }

    void processRatings(String rating, String numRatings, String likedPercent,
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
            }
        }


        if (ratingsByStars != null && ratingsByStars.length == 5) {
            for (int i = 0; i < ratingsByStars.length; i++) {
                String ratingsByStar = ratingsByStars[i];
                ratingsByStar = ratingsByStar.trim().replaceAll("[^\\d]", "");
                int ratingsByStarNumerical = 0;
                if (!ratingsByStar.isBlank()) {
                    ratingsByStarNumerical = Integer.parseInt(ratingsByStar);
                }
                switch (i) {
                    case 0 -> ratingObj.setFiveStarRatings(ratingsByStarNumerical);
                    case 1 -> ratingObj.setFourStarRatings(ratingsByStarNumerical);
                    case 2 -> ratingObj.setThreeStarRatings(ratingsByStarNumerical);
                    case 3 -> ratingObj.setTwoStarRatings(ratingsByStarNumerical);
                    case 4 -> ratingObj.setOneStarRatings(ratingsByStarNumerical);
                }
            }
        }


        ratingObj.setBook(book);
        book.setRating(ratingObj);
    }

    void setupRatings(int[] ratingsByStars, Book book) {
        Rating ratingObj;
        if (book.getRating() != null) {
            ratingObj = book.getRating();
        } else {
            ratingObj = new Rating();
        }
        if (ratingsByStars != null && ratingsByStars.length != 0) {
            if (ratingsByStars.length != 5) {
                throw new IllegalArgumentException("Invalid ratings by stars data");
            }
            for (int i = 0; i < ratingsByStars.length; i++) {
                int ratingsByStarNumerical = ratingsByStars[i];
                switch (i) {
                    case 0 -> ratingObj.setFiveStarRatings(ratingsByStarNumerical);
                    case 1 -> ratingObj.setFourStarRatings(ratingsByStarNumerical);
                    case 2 -> ratingObj.setThreeStarRatings(ratingsByStarNumerical);
                    case 3 -> ratingObj.setTwoStarRatings(ratingsByStarNumerical);
                    case 4 -> ratingObj.setOneStarRatings(ratingsByStarNumerical);
                }
            }

            int numRatings = ratingObj.getFiveStarRatings() + ratingObj.getFourStarRatings() +
                    ratingObj.getThreeStarRatings() + ratingObj.getTwoStarRatings() +
                    ratingObj.getOneStarRatings();
            ratingObj.setNumRatings(numRatings);

            float rating = Math.round(
                    (ratingObj.getFiveStarRatings() * 5 + ratingObj.getFourStarRatings() * 4 +
                            ratingObj.getThreeStarRatings() * 3 + ratingObj.getTwoStarRatings() * 2 +
                            ratingObj.getOneStarRatings()) * 100f / numRatings) / 100f;
            ratingObj.setRating(rating);

            int likedPercent = Math.round(
                    (ratingObj.getFiveStarRatings() + ratingObj.getFourStarRatings() +
                            ratingObj.getThreeStarRatings()) * 100f / numRatings );
            ratingObj.setLikedPercent(likedPercent);
        }

        ratingObj.setBook(book);
        book.setRating(ratingObj);
    }

    void addRatingToBook(int star, Book book) {
        if (star < 1 || star > 5) {
            throw new IllegalArgumentException("Invalid star rating. Must be between 1 and 5.");
        }
        Rating rating = book.getRating();
        if (rating == null) {
            System.out.println("Rating is null, creating a new one.");
            rating = new Rating();
            rating.setFiveStarRatings(0);
            rating.setFourStarRatings(0);
            rating.setThreeStarRatings(0);
            rating.setTwoStarRatings(0);
            rating.setOneStarRatings(0);
            book.setRating(rating);
        }
        switch (star) {
            case 5 -> rating.setFiveStarRatings(rating.getFiveStarRatings() + 1);
            case 4 -> rating.setFourStarRatings(rating.getFourStarRatings() + 1);
            case 3 -> rating.setThreeStarRatings(rating.getThreeStarRatings() + 1);
            case 2 -> rating.setTwoStarRatings(rating.getTwoStarRatings() + 1);
            default -> rating.setOneStarRatings(rating.getOneStarRatings() + 1);
        }
        int[] ratingsByStars = {
                rating.getFiveStarRatings(),
                rating.getFourStarRatings(),
                rating.getThreeStarRatings(),
                rating.getTwoStarRatings(),
                rating.getOneStarRatings()
        };
        setupRatings(ratingsByStars, book);
    }
}
