package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Author;
import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookAuthor;
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

    @Transactional
    public void processCsv(MultipartFile file) throws IOException {
        List<Book> books = new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        Map<String, Author> processedAuthors = new HashMap<>();
        List<BookAuthor> bookAuthors = new ArrayList<>();
        Set<String> existingAuthorNames = new HashSet<>(authorService.getAllAuthorNames());
        Set<String> seenAuthorNames = new HashSet<>();
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
                    String[] authorNames = record.get("author").trim().split(",\\s*");

                    if (existingBookIds.contains(bookId) || seenBookIds.contains(bookId)) {
                        System.out.println("Duplicate book found in CSV or DB, skipping: " + bookId);
                        continue;
                    }


                    description = description.substring(0, Math.min(description.length(), 2500));

                    Book book = new Book();
                    book.setBookId(bookId);
                    book.setTitle(title);
                    book.setDescription(description);
                    book.setSeries(series);

                    for (String name : authorNames) {
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
                    books.add(book);
                    seenBookIds.add(bookId); // Prevent re-processing within the same file
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error processing record: " + record, e);
                }

                if (books.size() > 500 || authors.size() > 500){
                    saveBatch(books, authors, bookAuthors);
                }
            }

            saveBatch(books, authors, bookAuthors);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to process CSV file", ex);

        }
    }

    private void saveBatch(List<Book> books, List<Author> authors, List<BookAuthor> bookAuthors) {
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
    }

}