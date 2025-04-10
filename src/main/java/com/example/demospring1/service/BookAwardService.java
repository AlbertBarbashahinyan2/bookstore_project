package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Award;
import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookAward;

import com.example.demospring1.persistence.repository.BookAwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookAwardService {
    private final BookAwardRepository bookAwardRepository;

    public void save(BookAward bookAward) {
        bookAwardRepository.save(bookAward);
    }

    public void saveAll(List<BookAward> bookAwards) {
        bookAwardRepository.saveAll(bookAwards);
    }

    void setupBookAwards(Book book, List<BookAward> bookAwards, Award award, Integer year) {
        // Create the BookAward relationship
        BookAward bookAward = new BookAward();
        bookAward.setBook(book);
        bookAward.setAward(award);
        bookAward.setYearReceived(year);
        bookAwards.add(bookAward);
    }
}
