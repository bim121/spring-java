package org.example.repositories;

import java.util.List;
import org.example.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategories_Id(Long categoryId);
}
