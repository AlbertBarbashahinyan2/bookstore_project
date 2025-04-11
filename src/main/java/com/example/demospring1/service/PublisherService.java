package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.*;
import com.example.demospring1.persistence.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final BookPublisherService bookPublisherService;

    public void save(Publisher publisher) {
        publisherRepository.save(publisher);
    }

    public List<String> getAllPublisherNames() {
        return publisherRepository.findAllPublisherNames();
    }

    public List<Publisher> findAll() {
        return publisherRepository.findAll();
    }

    public Publisher findByName(String name) {
        return publisherRepository.findByName(name);
    }

    public void saveAll(List<Publisher> publishers) {
        publisherRepository.saveAll(publishers);
    }

    void processPublishersAndBookPublishers(String[] publisherNames, Map<String,
            Publisher> processedPublishers, List<Publisher> publishers, Book book,
                                            List<BookPublisher> bookPublishers) {
        for (String name : publisherNames) {
            name = name.trim();
            if (name.isBlank()) {
                LOGGER.warning("Empty or blank publisher name found, skipping.");
                continue;
            }

            Publisher publisher;
            if (processedPublishers.containsKey(name)) {
                publisher = processedPublishers.get(name);
            } else {
                // Create a new transient Publisher and cache it
                publisher = new Publisher();
                publisher.setName(name);
                publishers.add(publisher);
                processedPublishers.put(name, publisher);
            }
            processedPublishers.put(name, publisher); // Cache the publisher for future use

            bookPublisherService.setupBookPublishers(book, bookPublishers, publisher);

        }

    }
}
