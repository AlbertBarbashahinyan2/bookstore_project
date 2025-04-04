package com.example.demospring1.service;

import com.example.demospring1.persistence.entity.Book;
import com.example.demospring1.persistence.entity.BookAuthor;
import com.example.demospring1.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Book addBook(String name, String title, List<BookAuthor> authors) {
        Book book = new Book();
        book.setBookId(name);
        book.setTitle(title);
        book.setAuthors(authors);
        return bookRepository.save(book);
    }

    public Book getBook(String bookId) {
        bookId = bookId.trim();
        return bookRepository.getByBookId(bookId);
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

        book.setSeries(series.isBlank() ? null : series);

        if (pages.isBlank()) {
            book.setPages(null);
        }else {
            pages = pages.trim().replaceAll("[^\\d]", "");
            int pagesInt = Integer.parseInt(pages);
            book.setPages(pagesInt);
        }

        if (price.isBlank()) {
            book.setPrice(null);
        } else {
            price = price.trim().replaceAll("[^\\d.]", "");
            int pointIndex = price.lastIndexOf(".");
            if (pointIndex != -1) {
                price = price.substring(0, pointIndex).replaceAll("\\.", "") + price.substring(pointIndex);
            }
            book.setPrice(Float.parseFloat(price));
        }

        book.setLanguage(language.isBlank() ? null : language);
        book.setEdition(edition.isBlank() ? null : edition);
        book.setBookFormat(bookFormat.isBlank() ? null : bookFormat);
        book.setIsbn(isbn.equals("9999999999999") ? null : isbn);

        return book;
    }
}
