package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookPublisher;
import com.example.demospring1.persistence.entity.Publisher;
import com.example.demospring1.persistence.repository.BookPublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookPublisherService {
    private final BookPublisherRepository bookPublisherRepository;

    public void save(BookPublisher bookPublisher) {
        bookPublisherRepository.save(bookPublisher);
    }

    public void saveAll(List<BookPublisher> bookPublisher) {
        bookPublisherRepository.saveAll(bookPublisher);
    }

    static void setupBookPublishers(Book book, List<BookPublisher> bookPublishers, Publisher publisher) {
        // Create the BookAuthor relationship
        BookPublisher bookPublisher = new BookPublisher();
        bookPublisher.setBook(book);
        bookPublisher.setPublisher(publisher);
        bookPublishers.add(bookPublisher);
    }
}
