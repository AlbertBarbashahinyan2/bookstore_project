package com.example.demospring1.service;

import com.example.demospring1.exception.AuthorNotFoundException;
import com.example.demospring1.exception.BookAlreadyExistsException;
import com.example.demospring1.exception.BookNotFoundException;
import com.example.demospring1.persistence.entity.*;

import com.example.demospring1.persistence.entity.Character;
import com.example.demospring1.persistence.repository.AuthorRepository;
import com.example.demospring1.persistence.repository.BookRepository;
import com.example.demospring1.service.dto.BookDto;
import com.example.demospring1.service.imagehandler.BookImageService;
import com.example.demospring1.service.mapper.BookMapper;
import com.example.demospring1.service.parser.DateParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookAuthorService bookAuthorService;
    private final GenreService genreService;
    private final BookGenreService bookGenreService;
    private final PublisherService publisherService;
    private final BookPublisherService bookPublisherService;
    private final AwardService awardService;
    private final BookAwardService bookAwardService;
    private final SettingService settingService;
    private final BookSettingService bookSettingService;
    private final CharacterService characterService;
    private final BookCharacterService bookCharacterService;
    private final RatingService ratingService;
    private final BookMapper bookMapper;
    private final DateParser dateParser;
    private final AuthorRepository authorRepository;
    private final BookImageService bookImageService;

    public List<BookDto> findAll(Specification<Book> spec) {
        List<Book> books = bookRepository.findAll(spec);
        if (books.isEmpty()) {
            throw new BookNotFoundException("No books found");
        }
        return bookMapper.toDtos(books);
    }

    @Transactional
    public void addRatingToBook(int star, String bookId) {
        Book book = bookRepository.getByBookId(bookId);
        if (book == null) {
            throw new BookNotFoundException(bookId);
        }
        ratingService.addRatingToBook(star, book);
    }

    @Transactional
    public void createBookFromDto(BookDto dto) {
        if (bookRepository.existsByBookId(dto.getBookId())) {
            throw new BookAlreadyExistsException(dto.getBookId());
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
                dto.getIsbn(),
                String.valueOf(dto.getBbeVotes()),
                String.valueOf(dto.getBbeScore()),
                dto.getPublishDate(),
                dto.getFirstPublishDate(),
                dto.getImageUrl()
        );

        if (dto.getAuthors() != null) {
            List<BookAuthor> bookAuthors = new ArrayList<>();
            for (String name : dto.getAuthors()) {
                if (name == null || name.trim().isBlank()) continue;
                String cleanName = name.trim();
                Author author = authorRepository.findByName(cleanName);
                if (author == null) {
                    throw new AuthorNotFoundException(cleanName);
                }

                bookAuthorService.setupBookAuthors(book, bookAuthors, author);

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

                bookGenreService.setupBookGenres(book, bookGenres, genre);

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

                bookPublisherService.setupBookPublishers(book, bookPublishers, publisher);

            }

            bookPublisherService.saveAll(bookPublishers);
        }

        if (dto.getAwards() != null) {

            List<BookAward> bookAwards = new ArrayList<>();

            Map<String, Integer> processedAwards = awardService.parseAwards(dto.getAwards().toArray(new String[0]));

            processedAwards.forEach((name, year) -> {
                if (name == null || name.trim().isBlank()) return;
                String cleanName = name.trim();


                Award award = awardService.findByName(cleanName);
                if (award == null) {
                    award = new Award();
                    award.setName(cleanName);
                    awardService.save(award);
                }

                bookAwardService.setupBookAwards(book, bookAwards, award, year);

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

                bookSettingService.setupBookSettings(book, bookSettings, setting);

            }

            bookSettingService.saveAll(bookSettings);
        }

        if (dto.getCharacters() != null) {

            List<BookCharacter> bookCharacters = new ArrayList<>();

            for (String name : dto.getCharacters()) {
                if (name == null || name.trim().isBlank()) continue;
                String cleanName = name.trim();


                Character character = characterService.findByName(cleanName);
                if (character == null) {
                    character = new Character();
                    character.setName(cleanName);
                    characterService.save(character);
                }

                bookCharacterService.setupBookCharacters(book, bookCharacters, character);

            }

            bookCharacterService.saveAll(bookCharacters);
        }


        if (dto.getRatingsByStars() == null || dto.getRatingsByStars().length != 5) {
            dto.setRatingsByStars(new int[5]);
        }
        ratingService.setupRatings(dto.getRatingsByStars(), book);

        bookRepository.save(book);

    }

    public BookDto getBookDto(String bookId) {
        bookId = bookId.trim();
        if (bookRepository.getByBookId(bookId) == null) {
            throw new BookNotFoundException(bookId);
        }
        return bookMapper.toDto(bookRepository.getByBookId(bookId));
    }

    public Book getBook(String bookId) {
        bookId = bookId.trim();
        if (bookRepository.getByBookId(bookId) == null) {
            throw new BookNotFoundException(bookId);
        }
        return bookRepository.getByBookId(bookId);
    }

    public byte[] getBookCover(String bookId) {
        bookId = bookId.trim();
        if (bookRepository.getByBookId(bookId) == null) {
            throw new BookNotFoundException(bookId);
        }
        Book book = bookRepository.getByBookId(bookId);
        Path path = Path.of(book.getCoverImagePath());
        System.out.println(path);
        return bookImageService.getImage(path);
    }

    public List<String> getAllBookIds() {
        return bookRepository.findAllBookIds();
    }

    public void saveAllBooks(List<Book> books) {
        bookRepository.saveAll(books);
    }

    @Transactional
    public void deleteBook(String bookId) {
        if (bookRepository.getByBookId(bookId) == null) {
            throw new BookNotFoundException(bookId);
        }
        bookRepository.deleteByBookId(bookId);
    }

    public void saveAll(List<Book> books) {
        bookRepository.saveAll(books);
    }

    Book setupBook(String bookId, String title, String description,
                   String series, String pages, String price, String language,
                   String edition, String bookFormat, String isbn, String bbeVotes,
                   String bbeScore, String publishDate, String firstPublishDate, String imageUrl) {
        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle(title);

        description = description.substring(0, Math.min(description.length(), 2500));
        book.setDescription(description);

        book.setSeries(series == null || series.isBlank() ? null : series);

        if (pages == null || pages.isBlank()) {
            book.setPages(null);
        } else {
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

        if (bbeVotes == null || bbeVotes.isBlank()) {
            book.setBbeVotes(null);
        } else {
            bbeVotes = bbeVotes.trim().replaceAll("[^\\d]", "");
            int bbeVotesInt = Integer.parseInt(bbeVotes);
            book.setBbeVotes(bbeVotesInt);
        }

        if (bbeScore == null || bbeScore.isBlank()) {
            book.setBbeScore(null);
        } else {
            bbeScore = bbeScore.trim().replaceAll("[^\\d]", "");
            int bbeScoreInt = Integer.parseInt(bbeScore);
            book.setBbeScore(bbeScoreInt);
        }

        if (publishDate == null || publishDate.isBlank()) {
            book.setPublishDate(null);
        } else {
            LocalDate publishDateParsed = dateParser.parseDateFlexible(publishDate);
            book.setPublishDate(publishDateParsed);
        }

        if (firstPublishDate == null || firstPublishDate.isBlank()) {
            book.setFirstPublishDate(null);
        } else {
            LocalDate firstPublishDateParsed = dateParser.parseDateFlexible(firstPublishDate);
            book.setFirstPublishDate(firstPublishDateParsed);
        }

        book.setLanguage(language == null || language.isBlank() ? null : language);
        book.setEdition(edition == null || edition.isBlank() ? null : edition);
        book.setBookFormat(bookFormat == null || bookFormat.isBlank() ? null : bookFormat);
        book.setIsbn(isbn.equals("9999999999999") ? null : isbn);


        final ExecutorService executorService = Executors.newFixedThreadPool(20);
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            Path path = bookImageService.getImagePath(bookId, imageUrl);
            if (path != null) {
                bookImageService.processImage(imageUrl, path);
//                book.setCoverImagePath(path.toString());
            } else {
                log.warn("Setting cover image path to null for book: {}", bookId);
//                book.setCoverImagePath(null);
            }
        }, executorService);

        book.setCoverImagePath("book-images/" + bookId + "/original.jpg");
        book.setThumbnailImagePath("book-images/" + bookId + "/thumbnail.jpg");
        return book;
    }
}
