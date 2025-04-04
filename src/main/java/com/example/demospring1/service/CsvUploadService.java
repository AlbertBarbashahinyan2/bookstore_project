package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class CsvUploadService {

    static final Logger LOGGER = Logger.getLogger(CsvUploadService.class.getName());

    private final BookService bookService;
    private final AuthorService authorService;
    private final BookAuthorService bookAuthorService;
    private final GenreService genreService;
    private final BookGenreService bookGenreService;
    private final PublisherService publisherService;
    private final BookPublisherService bookPublisherService;
    private final SettingService settingService;
    private final BookSettingService bookSettingService;
    private final AwardService awardService;
    private final BookAwardService bookAwardService;
    private final RatingService ratingService;


    @Transactional
    public void processCsv(MultipartFile file) throws IOException {
        List<Book> books = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        List<Publisher> publishers = new ArrayList<>();
        List<Setting> settings = new ArrayList<>();
        List<Award> awards = new ArrayList<>();
        Map<String, Author> processedAuthors = new HashMap<>();
        Map<String, Genre> processedGenres = new HashMap<>();
        Map<String, Publisher> processedPublishers = new HashMap<>();
        Map<String, Setting> processedSettings = new HashMap<>();
        Map<String, Award> processedAwards = new HashMap<>();
        List<BookAuthor> bookAuthors = new ArrayList<>();
        List<BookGenre> bookGenres = new ArrayList<>();
        List<BookPublisher> bookPublishers = new ArrayList<>();
        List<BookSetting> bookSettings = new ArrayList<>();
        List<BookAward> bookAwards = new ArrayList<>();
        Set<String> existingAuthorNames = new HashSet<>(authorService.getAllAuthorNames());
        Set<String> existingGenreNames = new HashSet<>(genreService.getAllGenreNames());
        Set<String> existingPublisherNames = new HashSet<>(publisherService.getAllPublisherNames());
        Set<String> existingSettingNames = new HashSet<>(settingService.getAllSettingNames());
        Set<String> existingAwardNames = new HashSet<>(awardService.getAllAwardNames());
        Set<String> existingBookIds = new HashSet<>(bookService.getAllBookIds()); // Fetch all existing book IDs
        Set<String> seenBookIds = new HashSet<>(); // Track duplicates in the CSV

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreSurroundingSpaces()
                    .withQuote('"')
                    .withEscape(null) // Completely disable escaping
                    .withIgnoreEmptyLines()
                    .withQuoteMode(QuoteMode.NON_NUMERIC)
                    .withTrim());


            for (CSVRecord record : csvParser) {

                try {
                    // Add debug printing for the current record
                    System.out.println("Processing record number: " + record.getRecordNumber());

                    String bookId = record.get("bookId").trim();
                    String title = record.get("title").trim();
                    String description = record.get("description").trim();
                    String series = record.get("series").trim();
                    String pages = record.get("pages").trim();
                    String price = record.get("price").trim();
                    String language = record.get("language").trim();
                    String bookFormat = record.get("bookFormat").trim();
                    String edition = record.get("edition").trim();
                    String isbn = record.get("isbn").trim();
                    String rating = record.get("rating").trim();
                    String numRatings = record.get("numRatings").trim();
                    String likedPercent = record.get("likedPercent").trim();
                    String[] authorNames = record.get("author").trim().split(",\\s*");
                    String[] genreNames = record.get("genres").trim().replaceAll("[\\[\\]']", "").split(",\\s*");
                    String[] publisherNames = record.get("publisher").trim().split("/\\s*");
                    String[] settingNames = record.get("setting").trim().replaceAll("[\\[\\]']", "").split(",\\s*");
                    String[] awardNames = record.get("awards").trim().replaceAll("[\\[\\]]", "")
                            .replaceAll("(?<!\\\\)'(.*?)'(?!\\\\)", "$1")
                            .replaceAll("(?<!\\\\)\"(.*?)\"(?!\\\\)", "$1").split(",\\s*");
                    String[] ratingsByStars = record.get("ratingsByStars").trim().replaceAll("[\\[\\]']", "").split(",\\s*");

                    if (existingBookIds.contains(bookId) || seenBookIds.contains(bookId)) {
                        System.out.println("Duplicate book found in CSV or DB, skipping: " + bookId);
                        continue;
                    }

                    Book book = bookService.setupBook(bookId, title, description,
                            series, pages, price, language, edition, bookFormat, isbn);

                    authorService.processAuthorsAndBookAuthors(authorNames, processedAuthors,
                            existingAuthorNames, authors, book, bookAuthors);

                    genreService.processGenresAndBookGenres(genreNames, processedGenres,
                            existingGenreNames, genres, book, bookGenres);

                    publisherService.processPublishersAndBookPublishers(publisherNames,
                            processedPublishers, existingPublisherNames, publishers, book, bookPublishers);

                    settingService.processSettingsAndBookSettings(settingNames, processedSettings,
                            existingSettingNames, settings, book, bookSettings);

                    awardService.processAwardsAndBookAwards(awardNames, processedAwards,
                            existingAwardNames, awards, book, bookAwards);

                    ratingService.setupRatings( rating, numRatings, likedPercent, ratingsByStars, book);

                    books.add(book);
                    seenBookIds.add(bookId); // Prevent re-processing within the same file
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing record: " + record, e);
                }

                if (books.size() > 500 || authors.size() > 500 || bookAuthors.size() > 500
                        || genres.size() > 500 || bookGenres.size() > 1000 || publishers.size() > 500
                        || bookPublishers.size() > 500 || settings.size() > 500 || bookSettings.size() > 500
                        || awards.size() > 500 || bookAwards.size() > 500 ){
                    saveBatch(books, authors, bookAuthors, genres, bookGenres,
                            publishers, bookPublishers, settings, bookSettings,
                            awards, bookAwards);
                }
            }

            saveBatch(books, authors, bookAuthors, genres, bookGenres,
                    publishers, bookPublishers, settings, bookSettings,
                    awards, bookAwards);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to process CSV file", ex);

        }
    }

    private void saveBatch(
            List<Book> books,
            List<Author> authors,
            List<BookAuthor> bookAuthors,
            List<Genre> genres,
            List<BookGenre> bookGenres,
            List<Publisher> publishers,
            List<BookPublisher> bookPublishers,
            List<Setting> settings,
            List<BookSetting> bookSettings,
            List<Award> awards,
            List<BookAward> bookAwards
    ) {
        if (!authors.isEmpty()) {
            authorService.saveAll(authors);
            authors.clear(); // Clear to release memory
        }
        if (!books.isEmpty()) {
            bookService.saveAll(books);
            books.clear(); // Clear to release memory
        }

        if (!bookAuthors.isEmpty()) {
            bookAuthorService.saveAll(bookAuthors);
            bookAuthors.clear(); // Clear to release memory
        }

        if (!genres.isEmpty()) {
            genreService.saveAll(genres);
            genres.clear(); // Clear to release memory
        }

        if (!bookGenres.isEmpty()) {
            bookGenreService.saveAll(bookGenres);
            bookGenres.clear(); // Clear to release memory
        }

        if (!publishers.isEmpty()) {
            publisherService.saveAll(publishers);
            publishers.clear(); // Clear to release memory
        }

        if (!bookPublishers.isEmpty()) {
            bookPublisherService.saveAll(bookPublishers);
            bookPublishers.clear(); // Clear to release memory
        }

        if (!settings.isEmpty()) {
            settingService.saveAll(settings);
            settings.clear(); // Clear to release memory
        }

        if (!bookSettings.isEmpty()) {
            bookSettingService.saveAll(bookSettings);
            bookSettings.clear(); // Clear to release memory
        }

        if (!awards.isEmpty()) {
            awardService.saveAll(awards);
            awards.clear(); // Clear to release memory
        }

        if (!bookAwards.isEmpty()) {
            bookAwardService.saveAll(bookAwards);
            bookAwards.clear(); // Clear to release memory
        }
    }
}