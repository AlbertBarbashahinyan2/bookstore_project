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
}
