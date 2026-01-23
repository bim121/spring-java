package org.example.repositories;

import java.util.List;
import java.util.Optional;
import org.example.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);
}
