package com.example.demospring1.persistence.specification;

import com.example.demospring1.persistence.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorSpecification {

    public Specification<Author> withCriteria(com.example.demospring1.service.criteria.AuthorSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);
            Join<Author, BookAuthor> bookAuthorJoin = root.join("books");
            Join<BookAuthor, Book> bookJoin = bookAuthorJoin.join("book");

            // Name filter
            if (criteria.getName() != null) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + criteria.getName().toLowerCase() + "%"
                ));
            }

            // BookTitle filter
            if (criteria.getBookTitle() != null && !criteria.getBookTitle().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(bookJoin.get("title")),
                        "%" + criteria.getBookTitle().toLowerCase() + "%"
                ));

            }

            // Award filter
            if (criteria.getAward() != null && !criteria.getAward().isEmpty()) {
                Join<Book, BookAward> bookAwardJoin = bookJoin.join("awards");
                Join<BookAward, Award> awardJoin = bookAwardJoin.join("award");

                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(awardJoin.get("name")),
                        "%" + criteria.getAward().toLowerCase() + "%"
                ));

            }

            // Minimum books written filter
            if (criteria.getMinBooks() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("bookCount"),
                        criteria.getMinBooks()
                ));

            }

            // Maximum books written filter
            if (criteria.getMaxBooks() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("bookCount"),
                        criteria.getMaxBooks()
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
