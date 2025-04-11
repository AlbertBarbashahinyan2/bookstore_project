package com.example.demospring1.service.parser;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class DateParser {

    private static final List<DateTimeFormatter> FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
            DateTimeFormatter.ofPattern("M/d/yyyy"),
            DateTimeFormatter.ofPattern("M/d/yy"),
            DateTimeFormatter.ofPattern("MMMM d yyyy")
    );

    public LocalDate parseDateFlexible(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) return null;

        String normalized = normalizeOrdinalDate(rawDate.trim());

        // Check if it's just a year
        if (normalized.matches("\\d{4}")) {
            return LocalDate.of(Integer.parseInt(normalized), 1, 1);
        }

        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDate.parse(normalized, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }

        return null;
    }


    private static String normalizeOrdinalDate(String rawDate) {
        // Remove 'st', 'nd', 'rd', 'th' from day numbers
        return rawDate.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
    }
}
