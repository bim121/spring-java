package org.example.services.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.model.Book;
import org.example.repositories.BookRepository;
import org.example.services.BookService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
