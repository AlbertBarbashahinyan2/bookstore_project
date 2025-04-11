package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Author;
import com.example.demospring1.persistence.entity.Award;
import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookAward;
import com.example.demospring1.persistence.repository.AwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class AwardService {
    private final AwardRepository awardRepository;
    private final BookAwardService bookAwardService;

    public void save(Award award) {
        awardRepository.save(award);
    }

    public Award findByName(String name) {
        return awardRepository.findByName(name);
    }

    public List<String> getAllAwardNames(){
        return awardRepository.findAllAwardNames();
    }

    public List<Award> findAll() {
        return awardRepository.findAll();
    }

    public void saveAll(List<Award> awards) {
        awardRepository.saveAll(awards);
    }

    void processAwardsAndBookAwards(String[] awardNames, Map<String, Award> processedAwards,
                                    List<Award> awards, Book book, List<BookAward> bookAwards) {
        Map<String, Integer> awardsWithYears = parseAwards(awardNames);
        awardsWithYears.forEach((name, year) -> {
            name = name.trim();
            if (name.isBlank()) {
                LOGGER.warning("Empty or blank award name found, skipping.");
                return;
            }

            Award award;
                if (processedAwards.containsKey(name)) {
                    award = processedAwards.get(name);
                } else {
                    // Create a new transient Award and cache it
                    award = new Award();
                    award.setName(name);
                    awards.add(award);
                    processedAwards.put(name, award); // Cache the award for future use
                }

            bookAwardService.setupBookAwards(book, bookAwards, award, year);

        });

    }

    Map<String, Integer> parseAwards(String[] awards) {
        Map<String, Integer> awardYearMap = new HashMap<>();

        Pattern pattern = Pattern.compile("(.*?)(\\((\\d{4})\\))$"); // Match anything before parentheses and year inside

        for (String award : awards) {
            Matcher matcher = pattern.matcher(award);

            if (matcher.find()) {
                String awardName = matcher.group(1).trim();
                String yearStr = matcher.group(3); // Extract year

                Integer year = null;
                try {
                    year = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    // If year is invalid or missing, leave it as null
                    System.out.println("Skipping invalid year for award: " + award);
                }

                awardYearMap.put(awardName, year);
            } else {
                // If there's no year found, set year as null
                awardYearMap.put(award, null);
            }
        }

        return awardYearMap;
    }
}
