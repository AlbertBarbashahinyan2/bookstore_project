package com.example.demospring1.service.mapper;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.service.dto.BookDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookMapper {

    public List<BookDto> toDtos(List<Book> books) {
        if (books == null) {
            return List.of(); // Return an empty list if books are null
        }
        List<BookDto> bookDtos = new ArrayList<>();
        for (Book book : books) {
            bookDtos.add(toDto(book));
        }
        return bookDtos;
    }

    public BookDto toDto(Book book) {
        if (book == null) {
            return null;
        }
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setBookId(book.getBookId());
        bookDto.setTitle(book.getTitle());
        bookDto.setDescription(book.getDescription());
        bookDto.setSeries(book.getSeries());
        bookDto.setPages(book.getPages());
        bookDto.setPrice(book.getPrice());
        bookDto.setLanguage(book.getLanguage());
        bookDto.setEdition(book.getEdition());
        bookDto.setBookFormat(book.getBookFormat());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setRatingsByStars(mapRatings(book));
        bookDto.setAuthors(mapAuthors(book));
        bookDto.setGenres(mapGenres(book));
        bookDto.setPublishers(mapPublishers(book));
        bookDto.setSettings(mapSettings(book));
        bookDto.setAwards(mapAwards(book));
        bookDto.setCharacters(mapCharacters(book));
        return bookDto;
    }

    private int[] mapRatings(Book book) {
        if (book.getRating() == null) {
            return new int[5]; // Return an array of zeros if rating is null
        }
        return new int[]{
                book.getRating().getFiveStarRatings(),
                book.getRating().getFourStarRatings(),
                book.getRating().getThreeStarRatings(),
                book.getRating().getTwoStarRatings(),
                book.getRating().getOneStarRatings()
        };
    }

    private List<String> mapAuthors(Book book) {
        if (book.getAuthors() == null) {
            return List.of(); // Return an empty list if authors are null
        }
        return book.getAuthors().stream()
            .map(bookAuthor -> bookAuthor.getAuthor().getName()).toList();
    }

    private List<String> mapGenres(Book book) {
        if (book.getGenres() == null) {
            return List.of(); // Return an empty list if genres are null
        }
        return book.getGenres().stream()
                .map(bookGenre -> bookGenre.getGenre().getName()).toList();
    }

    private List<String> mapPublishers(Book book) {
        if (book.getPublishers() == null) {
            return List.of(); // Return an empty list if publishers are null
        }
        return book.getPublishers().stream()
                .map(bookPublisher -> bookPublisher.getPublisher().getName()).toList();
    }

    private List<String> mapSettings(Book book) {
        if (book.getSettings() == null) {
            return List.of(); // Return an empty list if settings are null
        }
        return book.getSettings().stream()
                .map(bookSetting -> bookSetting.getSetting().getName()).toList();
    }

    private List<String> mapAwards(Book book) {
        if (book.getAwards() == null) {
            return List.of(); // Return an empty list if awards are null
        }
        return book.getAwards().stream()
                .map(bookAward -> bookAward.getAward().getName()).toList();
    }

    private List<String> mapCharacters(Book book) {
        if (book.getCharacters() == null) {
            return List.of(); // Return an empty list if characters are null
        }
        return book.getCharacters().stream()
                .map(bookCharacter -> bookCharacter.getCharacter().getName()).toList();
    }
}
