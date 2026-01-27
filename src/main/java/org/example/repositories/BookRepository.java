package org.example.repositories;

import java.util.Optional;
import org.example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIdAndDeletedFalse(Long id);
}
