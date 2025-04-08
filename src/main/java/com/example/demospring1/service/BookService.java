package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.*;

import com.example.demospring1.persistence.repository.BookRepository;
import com.example.demospring1.service.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.demospring1.service.AwardService.parseAwards;
import static com.example.demospring1.service.BookAuthorService.setupBookAuthors;
import static com.example.demospring1.service.BookAwardService.setupBookAwards;
import static com.example.demospring1.service.BookGenreService.setupBookGenres;
import static com.example.demospring1.service.BookPublisherService.setupBookPublishers;
import static com.example.demospring1.service.BookSettingService.setupBookSettings;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final BookAuthorService bookAuthorService;
    private final GenreService genreService;
    private final BookGenreService bookGenreService;
    private final PublisherService publisherService;
    private final BookPublisherService bookPublisherService;
    private final AwardService awardService;
    private final BookAwardService bookAwardService;
    private final SettingService settingService;
    private final BookSettingService bookSettingService;
    private final RatingService ratingService;

    @Transactional
    public void createBookFromDto(BookDto dto) {
        if (bookRepository.existsByBookId(dto.getBookId())) {
            throw new IllegalArgumentException("Book already exists with id: " + dto.getBookId());
        }

        Book book = setupBook(
                dto.getBookId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getSeries(),
                String.valueOf(dto.getPages()),
                String.valueOf(dto.getPrice()),
                dto.getLanguage(),
                dto.getEdition(),
                dto.getBookFormat(),
                dto.getIsbn()
        );



        if (dto.getAuthors() != null) {
            List<BookAuthor> bookAuthors = new ArrayList<>();
            for (String name : dto.getAuthors()) {
                if (name == null || name.trim().isBlank()) continue;
                String cleanName = name.trim();


                Author author = authorService.findByName(cleanName);
                if (author == null) {
                    author = new Author();
                    author.setName(cleanName);
                    authorService.save(author);
                }

                setupBookAuthors(book, bookAuthors, author);

            }

            bookAuthorService.saveAll(bookAuthors);
        }

        if (dto.getGenres() != null) {

            List<BookGenre> bookGenres = new ArrayList<>();

            for (String name : dto.getGenres()) {
                if (name == null || name.trim().isBlank()) continue;
                String cleanName = name.trim();


                Genre genre = genreService.findByName(cleanName);
                if (genre == null) {
                    genre = new Genre();
                    genre.setName(cleanName);
                    genreService.save(genre);
                }

                setupBookGenres(book, bookGenres, genre);

            }

            bookGenreService.saveAll(bookGenres);
        }

        if (dto.getPublishers() != null) {

            List<BookPublisher> bookPublishers = new ArrayList<>();

            for (String name : dto.getPublishers()) {
                if (name == null || name.trim().isBlank()) continue;
                String cleanName = name.trim();


                Publisher publisher = publisherService.findByName(cleanName);
                if (publisher == null) {
                    publisher = new Publisher();
                    publisher.setName(cleanName);
                    publisherService.save(publisher);
                }

                setupBookPublishers(book, bookPublishers, publisher);

            }

            bookPublisherService.saveAll(bookPublishers);
        }

        if (dto.getAwards() != null) {

            List<BookAward> bookAwards = new ArrayList<>();

            Map<String, Integer> processedAwards = parseAwards(dto.getAwards().toArray(new String[0]));

            processedAwards.forEach((name, year) -> {
                if (name == null || name.trim().isBlank()) return;
                String cleanName = name.trim();


                Award award = awardService.findByName(cleanName);
                if (award == null) {
                    award = new Award();
                    award.setName(cleanName);
                    awardService.save(award);
                }

                setupBookAwards(book, bookAwards, award, year);

            });

            bookAwardService.saveAll(bookAwards);
        }

        if (dto.getSettings() != null) {

            List<BookSetting> bookSettings = new ArrayList<>();

            for (String name : dto.getSettings()) {
                if (name == null || name.trim().isBlank()) continue;
                String cleanName = name.trim();


                Setting setting = settingService.findByName(cleanName);
                if (setting == null) {
                    setting = new Setting();
                    setting.setName(cleanName);
                    settingService.save(setting);
                }

                setupBookSettings(book, bookSettings, setting);

            }

            bookSettingService.saveAll(bookSettings);
        }


        if(dto.getRatingsByStars() == null) {
            dto.setRatingsByStars(new ArrayList<>());
        }
        ratingService.setupRatings(dto.getRating(), dto.getNumRatings(),
                dto.getLikedPercent(), dto.getRatingsByStars().toArray(new String[0]), book);

        bookRepository.save(book);
    }


    public Book getBookByBookId(String bookId) {
        bookId = bookId.trim();
        return bookRepository.getByBookId(bookId);
    }

    public Book getBook(Long id) {
        return bookRepository.findBookById(id);
    }

    public List<String> getAllBookIds() {
        return bookRepository.findAllBookIds();
    }

    public void saveAllBooks(List<Book> books) {
        bookRepository.saveAll(books);
    }

    public void deleteBook(Long id) {

        bookRepository.deleteById(id);
    }

    public void saveAll(List<Book> books) {
        bookRepository.saveAll(books);
    }

    Book setupBook(String bookId, String title, String description,
                          String series, String pages, String price, String language,
                          String edition, String bookFormat, String isbn) {
        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle(title);

        description = description.substring(0, Math.min(description.length(), 2500));
        book.setDescription(description);

        book.setSeries(series == null || series.isBlank() ? null : series);

        if (pages == null || pages.isBlank()) {
            book.setPages(null);
        }else {
            pages = pages.trim().replaceAll("[^\\d]", "");
            int pagesInt = Integer.parseInt(pages);
            book.setPages(pagesInt);
        }

        if (price == null || price.isBlank()) {
            book.setPrice(null);
        } else {
            price = price.trim().replaceAll("[^\\d.]", "");
            int pointIndex = price.lastIndexOf(".");
            if (pointIndex != -1) {
                price = price.substring(0, pointIndex).replaceAll("\\.", "") + price.substring(pointIndex);
            }
            book.setPrice(Float.parseFloat(price));
        }

        book.setLanguage(language == null || language.isBlank() ? null : language);
        book.setEdition(edition == null || edition.isBlank() ? null : edition);
        book.setBookFormat(bookFormat == null || bookFormat.isBlank() ? null : bookFormat);
        book.setIsbn(isbn.equals("9999999999999") ? null : isbn);

        return book;
    }
}
