package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.*;
import com.example.demospring1.persistence.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final SettingRepository settingRepository;
    private final BookSettingService bookSettingService;

    public void save(Setting setting) {
        settingRepository.save(setting);
    }

    public List<String> getAllSettingNames() {
        return settingRepository.findAllSettingNames();
    }

    public List<Setting> findAll() {
        return settingRepository.findAll();
    }

    public Setting findByName(String name) {
        return settingRepository.findByName(name);
    }

    public void saveAll(List<Setting> settings) {
        settingRepository.saveAll(settings);
    }

    void processSettingsAndBookSettings(String[] settingNames, Map<String, Setting> processedSettings,
                                        List<Setting> settings, Book book, List<BookSetting> bookSettings) {
        for (String name : settingNames) {
            name = name.trim();
            if (name.isBlank()) {
                LOGGER.warning("Empty or blank setting name found, skipping.");
                continue;
            }

            Setting setting;
            if (processedSettings.containsKey(name)) {
                // Author exists in database, fetch and cache it
                setting = processedSettings.get(name);
            } else {
                // Create a new transient Setting and cache it
                setting = new Setting();
                setting.setName(name);
                settings.add(setting);
                processedSettings.put(name, setting); // Cache the setting for future use
            }

            bookSettingService.setupBookSettings(book, bookSettings, setting);
        }
    }
}
