package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Setting;
import com.example.demospring1.persistence.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
