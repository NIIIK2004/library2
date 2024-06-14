package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.repo.BookRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepo bookRepo;

    public void saveBook(Book book) {
        bookRepo.save(book);
    }

    public List<Book> findAllBooks() {
        return bookRepo.findAll();
    }

    public List<Book> findBooksByUser(User user) {
        return bookRepo.findByUser(user);
    }
}
