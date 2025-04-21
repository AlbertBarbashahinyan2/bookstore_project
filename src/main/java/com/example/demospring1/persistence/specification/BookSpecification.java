package com.example.demospring1.persistence.specification;

import com.example.demospring1.persistence.entity.*;
import com.example.demospring1.persistence.entity.Character;
import com.example.demospring1.service.searchcriteria.BookSearchCriteria;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class BookSpecification {

    public static Specification<Book> withCriteria(BookSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // BookId filter
            if (criteria.getBookId() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("bookId")),
                        "%" + criteria.getBookId().toLowerCase() + "%"
                ));
            }

            // Title filter
            if (criteria.getTitle() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + criteria.getTitle().toLowerCase() + "%"
                ));
            }

            // Description filter
            if (criteria.getDescription() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + criteria.getDescription().toLowerCase() + "%"
                ));
            }

            // Series filter
            if (criteria.getSeries() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("series")),
                        "%" + criteria.getSeries().toLowerCase() + "%"
                ));
            }

            // Minimum pages filter
            if (criteria.getMinPages() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("pages"),
                        criteria.getMinPages()
                ));
            }

            // Maximum pages filter
            if (criteria.getMaxPages() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("pages"),
                        criteria.getMaxPages()
                ));
            }

            // Language filter
            if (criteria.getLanguage() != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("language")),
                        criteria.getLanguage().toLowerCase()
                ));
            }

            // Edition filter
            if (criteria.getEdition() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("edition")),
                        "%" + criteria.getEdition().toLowerCase() + "%"
                ));
            }

            // BookFormat filter
            if (criteria.getBookFormat() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("bookFormat")),
                        "%" + criteria.getBookFormat().toLowerCase() + "%"
                ));
            }

            // Isbn filter
            if (criteria.getIsbn() != null) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("isbn")),
                        criteria.getIsbn().toLowerCase()
                ));
            }

            // Minimum numRatings filter
            if (criteria.getMinNumRatings() != null || criteria.getMaxNumRatings() != null
                    || criteria.getMinLikedPercent() != null || criteria.getMaxLikedPercent() != null
            || criteria.getMinRating() != null || criteria.getMaxRating() != null ){

                Join<Book, Rating> ratingJoin = root.join("rating", JoinType.LEFT);

                // Minimum Rating filter
                if (criteria.getMinRating() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                            ratingJoin.get("rating"),
                            criteria.getMinRating()
                    ));
                }

                // Maximum Rating filter
                if (criteria.getMaxRating() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(
                            ratingJoin.get("rating"),
                            criteria.getMaxRating()
                    ));
                }

                // Minimum numRatings filter
                if (criteria.getMinNumRatings() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                            ratingJoin.get("numRatings"),
                            criteria.getMaxNumRatings()
                    ));
                }

                // Maximum numRatings filter
                if (criteria.getMaxRating() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(
                            ratingJoin.get("rating"),
                            criteria.getMaxRating()
                    ));
                }

                // Minimum likedPercent filter
                if (criteria.getMinLikedPercent() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                            ratingJoin.get("likedPercent"),
                            criteria.getMinLikedPercent()
                    ));
                }

                // Maximum likedPercent filter
                if (criteria.getMaxLikedPercent() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                            ratingJoin.get("likedPercent"),
                            criteria.getMaxLikedPercent()
                    ));
                }

            }

            // Author filter
            if (criteria.getAuthor() != null && !criteria.getAuthor().isEmpty()) {
                Join<Book, BookAuthor> bookAuthorJoin = root.join("authors");
                Join<BookAuthor, Author> authorJoin = bookAuthorJoin.join("author");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(authorJoin.get("name")),
                                "%" + criteria.getAuthor().toLowerCase() + "%"
                ));

            }

            // Genre filter
            if (criteria.getGenre() != null && !criteria.getGenre().isEmpty()) {
                Join<Book, BookGenre> bookGenreJoin = root.join("genres");
                Join<BookGenre, Genre> genreJoin = bookGenreJoin.join("genre");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(genreJoin.get("name")),
                        "%" + criteria.getGenre().toLowerCase() + "%"
                ));
            }

            // Publisher filter
            if (criteria.getPublisher() != null && !criteria.getPublisher().isEmpty()) {
                Join<Book, BookPublisher> bookPublisherJoin = root.join("publishers");
                Join<BookPublisher, Publisher> publisherJoin = bookPublisherJoin.join("publisher");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(publisherJoin.get("name")),
                        "%" + criteria.getPublisher().toLowerCase() + "%"
                ));            }

            // Award filter
            if (criteria.getAward() != null && !criteria.getAward().isEmpty()) {
                Join<Book, BookAward> bookAwardJoin = root.join("awards");
                Join<BookAward, Award> awardJoin = bookAwardJoin.join("award");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(awardJoin.get("name")),
                        "%" + criteria.getAward().toLowerCase() + "%"
                ));            }

            // Setting filter
            if (criteria.getSetting() != null && !criteria.getSetting().isEmpty()) {
                Join<Book, BookSetting> bookSettingJoin = root.join("settings");
                Join<BookSetting, Setting> settingJoin = bookSettingJoin.join("setting");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(settingJoin.get("name")),
                        "%" + criteria.getSetting().toLowerCase() + "%"
                ));
            }

            // Character filter
            if (criteria.getCharacter() != null && !criteria.getCharacter().isEmpty()) {
                Join<Book, BookCharacter> bookCharacterJoin = root.join("characters");
                Join<BookCharacter, Character> characterJoin = bookCharacterJoin.join("character");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(characterJoin.get("name")),
                        "%" + criteria.getCharacter().toLowerCase() + "%"
                ));
            }

            // Minimum bbeVotes filter
            if (criteria.getMinBbeVotes() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("bbeVotes"),
                        criteria.getMinBbeVotes()
                ));
            }

            // Maximum bbeVotes filter
            if (criteria.getMaxBbeVotes() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("bbeVotes"),
                        criteria.getMaxBbeVotes()
                ));
            }

            // Minimum bbeScore filter
            if (criteria.getMinBbeScore() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("bbeScore"),
                        criteria.getMinBbeScore()
                ));
            }

            // Maximum bbeScore filter
            if (criteria.getMaxBbeScore() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("bbeScore"),
                        criteria.getMaxBbeScore()
                ));
            }

            // publishDate filters
            if (criteria.getPublishDate() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("publishDate"),
                        criteria.getPublishDate()
                ));
            }

            if (criteria.getPublishDateStart() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("publishDate"),
                        criteria.getPublishDateStart()
                ));
            }

            if (criteria.getPublishDateEnd() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("publishDate"),
                        criteria.getPublishDateEnd()
                ));
            }

            // First publish date filters
            if (criteria.getFirstPublishDate() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("firstPublishDate"),
                        criteria.getFirstPublishDate()
                ));
            }

            if (criteria.getFirstPublishDateStart() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("firstPublishDate"),
                        criteria.getFirstPublishDateStart()
                ));
            }

            if (criteria.getFirstPublishDateEnd() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("firstPublishDate"),
                        criteria.getFirstPublishDateEnd()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
