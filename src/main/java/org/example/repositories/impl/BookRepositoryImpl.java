package org.example.repositories.impl;

import java.util.ArrayList;
import java.util.List;
import org.example.model.Book;
import org.example.repositories.BookRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private List<Book> books = new ArrayList<>();

    @Override
    public Book save(Book book) {
        books.add(book);
        return book;
    }

    @Override
    public List<Book> findAll() {
        return books;
    }
}
