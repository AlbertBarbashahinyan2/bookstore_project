package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Publisher;
import com.example.demospring1.persistence.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;

    public void save(Publisher publisher) {
        publisherRepository.save(publisher);
    }

    public List<String> getAllPublisherNames(){
        return publisherRepository.findAllPublisherNames();
    }

    public Publisher findByName(String name) {
        return publisherRepository.findByName(name);
    }

    public void saveAll(List<Publisher> publishers) {
        publisherRepository.saveAll(publishers);
    }
}
