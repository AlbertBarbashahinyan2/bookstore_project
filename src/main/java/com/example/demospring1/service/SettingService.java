package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.*;
import com.example.demospring1.persistence.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.demospring1.service.BookSettingService.setupBookSettings;
import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class SettingService {
    
    private final SettingRepository settingRepository;

    public void save(Setting setting) {
        settingRepository.save(setting);
    }

    public List<String> getAllSettingNames(){
        return settingRepository.findAllSettingNames();
    }

    public Setting findByName(String name) {
        return settingRepository.findByName(name);
    }

    public void saveAll(List<Setting> settings) {
        settingRepository.saveAll(settings);
    }

    void processSettingsAndBookSettings(String[] settingNames, Map<String, Setting> processedSettings,
                                    Set<String> existingSettingNames, List<Setting> settings, Book book,
                                    List<BookSetting> bookSettings) {
        for (String name : settingNames) {
            name = name.trim();
            if (name.isBlank()) {
                LOGGER.warning("Empty or blank setting name found, skipping.");
                continue;
            }

            // Check if author is already in processedAuthors
            Setting setting = processedSettings.get(name);
            if (setting == null) {
                if (existingSettingNames.contains(name)) {
                    // Author exists in database, fetch and cache it
                    setting = findByName(name);
                } else {
                    // Create a new transient Author and cache it
                    setting = new Setting();
                    setting.setName(name);
                    settings.add(setting);
                }
                processedSettings.put(name, setting); // Cache the author for future use
            }

            setupBookSettings(book, bookSettings, setting);

        }
    }
}
