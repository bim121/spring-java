package org.example.reporitories;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import org.example.model.Book;
import org.example.repositories.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("saveBook_ShouldPersist")
    void saveBook_ShouldPersist() {
        Book book = new Book();
        book.setTitle("Test");
        book.setAuthor("Author");
        book.setIsbn("1234567890");
        book.setPrice(new BigDecimal("19.99"));
        Book saved = bookRepository.save(book);
        assertNotNull(saved.getId());
    }
}
