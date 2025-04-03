package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.BookSetting;
import com.example.demospring1.persistence.repository.BookSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSettingService {
    private final BookSettingRepository bookSettingRepository;

    public void save(BookSetting bookSetting) {
        bookSettingRepository.save(bookSetting);
    }

    public void saveAll(List<BookSetting> bookSetting) {
        bookSettingRepository.saveAll(bookSetting);
    }
}
