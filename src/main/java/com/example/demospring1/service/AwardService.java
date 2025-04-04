package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Award;
import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookAward;
import com.example.demospring1.persistence.repository.AwardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demospring1.service.BookAwardService.setupBookAwards;
import static com.example.demospring1.service.CsvUploadService.LOGGER;

@Service
@RequiredArgsConstructor
public class AwardService {
    private final AwardRepository awardRepository;

    public void save(Award award) {
        awardRepository.save(award);
    }

    public Award findByName(String name) {
        return awardRepository.findByName(name);
    }

    public List<String> getAllAwardNames(){
        return awardRepository.findAllAwardNames();
    }

    public void saveAll(List<Award> awards) {
        awardRepository.saveAll(awards);
    }

    void processAwardsAndBookAwards(String[] awardNames, Map<String, Award> processedAwards,
                                    Set<String> existingAwardNames, List<Award> awards, Book book,
                                    List<BookAward> bookAwards) {
        Map<String, Integer> awardsWithYears = parseAwards(awardNames);
        awardsWithYears.forEach((name, year) -> {
            name = name.trim();
            if (name.isBlank()) {
                LOGGER.warning("Empty or blank award name found, skipping.");
                return;
            }

            // Check if author is already in processedAuthors
            Award award = processedAwards.get(name);
            if (award == null) {
                if (existingAwardNames.contains(name)) {
                    // Author exists in database, fetch and cache it
                    award = findByName(name);
                } else {
                    // Create a new transient Author and cache it
                    award = new Award();
                    award.setName(name);
                    awards.add(award);
                }
                processedAwards.put(name, award); // Cache the author for future use
            }

            setupBookAwards(book, bookAwards, award, year);

        });

    }

    public static Map<String, Integer> parseAwards(String[] awards) {
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
