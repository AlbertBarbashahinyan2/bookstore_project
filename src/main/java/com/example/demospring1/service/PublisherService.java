package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.*;
import com.example.demospring1.persistence.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.demospring1.service.BookPublisherService.setupBookPublishers;
import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public void save(Publisher publisher) {
        publisherRepository.save(publisher);
    }

    public List<String> getAllPublisherNames() {
        return publisherRepository.findAllPublisherNames();
    }

    public Publisher findByName(String name) {
        return publisherRepository.findByName(name);
    }

    public void saveAll(List<Publisher> publishers) {
        publisherRepository.saveAll(publishers);
    }

    void processPublishersAndBookPublishers(String[] publisherNames, Map<String, Publisher> processedPublishers,
                                            Set<String> existingPublisherNames, List<Publisher> publishers, Book book,
                                            List<BookPublisher> bookPublishers) {
        for (String name : publisherNames) {
            name = name.trim();
            if (name.isBlank()) {
                LOGGER.warning("Empty or blank publisher name found, skipping.");
                continue;
            }

            // Check if author is already in processedAuthors
            Publisher publisher = processedPublishers.get(name);
            if (publisher == null) {
                if (existingPublisherNames.contains(name)) {
                    // Author exists in database, fetch and cache it
                    publisher = findByName(name);
                } else {
                    // Create a new transient Author and cache it
                    publisher = new Publisher();
                    publisher.setName(name);
                    publishers.add(publisher);
                }
                processedPublishers.put(name, publisher); // Cache the author for future use
            }

            setupBookPublishers(book, bookPublishers, publisher);

        }

    }
}
