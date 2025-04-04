package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.*;
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

    static void setupBookSettings(Book book, List<BookSetting> bookSettings, Setting setting) {
        // Create the BookAuthor relationship
        BookSetting bookSetting = new BookSetting();
        bookSetting.setBook(book);
        bookSetting.setSetting(setting);
        bookSettings.add(bookSetting);
    }
}
