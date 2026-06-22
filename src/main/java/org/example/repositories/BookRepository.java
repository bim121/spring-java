package org.example.repositories;

import java.util.List;
import org.example.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
