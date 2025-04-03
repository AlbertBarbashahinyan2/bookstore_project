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

    private static final Logger LOGGER = Logger.getLogger(CsvUploadService.class.getName());

    private final BookService bookService;
    private final AuthorService authorService;
    private final BookAuthorService bookAuthorService;
    private final GenreService genreService;
    private final BookGenreService bookGenreService;
    private final PublisherService publisherService;
    private final BookPublisherService bookPublisherService;

    @Transactional
    public void processCsv(MultipartFile file) throws IOException {
        List<Book> books = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        List<Genre> genres = new ArrayList<>();
        List<Publisher> publishers = new ArrayList<>();
        Map<String, Author> processedAuthors = new HashMap<>();
        Map<String, Genre> processedGenres = new HashMap<>();
        Map<String, Publisher> processedPublishers = new HashMap<>();
        List<BookAuthor> bookAuthors = new ArrayList<>();
        List<BookGenre> bookGenres = new ArrayList<>();
        List<BookPublisher> bookPublishers = new ArrayList<>();
        Set<String> existingAuthorNames = new HashSet<>(authorService.getAllAuthorNames());
        Set<String> existingGenreNames = new HashSet<>(genreService.getAllGenreNames());
        Set<String> existingPublisherNames = new HashSet<>(publisherService.getAllPublisherNames());
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
                    String[] authorNames = record.get("author").trim().split(",\\s*");
                    String[] genreNames = record.get("genres").trim().replaceAll("[\\[\\]']", "").split(",\\s*");
                    String[] publisherNames = record.get("publisher").trim().split("/\\s*");

                    if (existingBookIds.contains(bookId) || seenBookIds.contains(bookId)) {
                        System.out.println("Duplicate book found in CSV or DB, skipping: " + bookId);
                        continue;
                    }

                    Book book = bookService.setupBook(bookId, title, description,
                            series, pages, price, language, edition, bookFormat, isbn);

                    for (String name : authorNames) {
                        name = name.trim();
                        if (name.isBlank()) {
                            LOGGER.warning("Empty or blank author name found, skipping.");
                            continue;
                        }

                        // Check if author is already in processedAuthors
                        Author author = processedAuthors.get(name);
                        if (author == null) {
                            if (existingAuthorNames.contains(name)) {
                                // Author exists in database, fetch and cache it
                                author = authorService.findByName(name);
                            } else {
                                // Create a new transient Author and cache it
                                author = new Author();
                                author.setName(name);
                                authors.add(author);
                            }
                            processedAuthors.put(name, author); // Cache the author for future use
                        }

                        // Create the BookAuthor relationship
                        BookAuthor bookAuthor = new BookAuthor();
                        bookAuthor.setBook(book);
                        bookAuthor.setAuthor(author);
                        bookAuthors.add(bookAuthor);

                    }

                    for (String name : genreNames) {
                        name = name.trim();
                        if (name.isBlank()) {
                            LOGGER.warning("Empty or blank genre name found, skipping.");
                            continue;
                        }

                        // Check if author is already in processedAuthors
                        Genre genre = processedGenres.get(name);
                        if (genre == null) {
                            if (existingGenreNames.contains(name)) {
                                // Author exists in database, fetch and cache it
                                genre = genreService.findByName(name);
                            } else {
                                // Create a new transient Author and cache it
                                genre = new Genre();
                                genre.setName(name);
                                genres.add(genre);
                            }
                            processedGenres.put(name, genre); // Cache the author for future use
                        }

                        // Create the BookAuthor relationship
                        BookGenre bookGenre = new BookGenre();
                        bookGenre.setBook(book);
                        bookGenre.setGenre(genre);
                        bookGenres.add(bookGenre);

                    }

                    for (String name : publisherNames) {
                        name = name.trim();
                        if (name.isBlank()) {
                            LOGGER.warning("Empty or blank publisher name found, skipping.");
                            continue;
                        }

                        // Check if author is already in processedAuthors
                        Publisher publisher = processedPublishers.get(name);
                        if (publisher == null) {
                            if (existingPublisherNames.contains(name)) {
                                // Author exists in database, fetch and cache it
                                publisher = publisherService.findByName(name);
                            } else {
                                // Create a new transient Author and cache it
                                publisher = new Publisher();
                                publisher.setName(name);
                                publishers.add(publisher);
                            }
                            processedPublishers.put(name, publisher); // Cache the author for future use
                        }

                        // Create the BookAuthor relationship
                        BookPublisher bookPublisher = new BookPublisher();
                        bookPublisher.setBook(book);
                        bookPublisher.setPublisher(publisher);
                        bookPublishers.add(bookPublisher);

                    }

                    books.add(book);
                    seenBookIds.add(bookId); // Prevent re-processing within the same file
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing record: " + record, e);
                }

                if (books.size() > 500 || authors.size() > 500 || bookAuthors.size() > 500
                        || genres.size() > 500 || bookGenres.size() > 500 || publishers.size() > 500
                        || bookPublishers.size() > 500){
                    saveBatch(books, authors, bookAuthors, genres, bookGenres, publishers, bookPublishers);
                }
            }

            saveBatch(books, authors, bookAuthors, genres, bookGenres, publishers, bookPublishers);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to process CSV file", ex);

        }
    }

    private void saveBatch(List<Book> books, List<Author> authors, List<BookAuthor> bookAuthors,
                           List<Genre> genres, List<BookGenre> bookGenres, List<Publisher> publishers, List<BookPublisher> bookPublishers) {
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
    }

}