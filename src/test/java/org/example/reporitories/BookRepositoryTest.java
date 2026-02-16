package org.example.reporitories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.example.helpers.TestDataHelper;
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
        Book book = TestDataHelper.createBook();
        Book saved = bookRepository.save(book);
        assertNotNull(saved.getId());
    }

    @Test
    @DisplayName("findById_ExistingId_ReturnsBook")
    void findById_ExistingId_ReturnsBook() {
        Book book = TestDataHelper.createBook();
        Book saved = bookRepository.save(book);
        Optional<Book> found = bookRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals(saved.getTitle(), found.get().getTitle());
        assertEquals(saved.getAuthor(), found.get().getAuthor());
    }

    @Test
    @DisplayName("findById_NonExistingId_ReturnsEmpty")
    void findById_NonExistingId_ReturnsEmpty() {
        Optional<Book> found = bookRepository.findById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("deleteById_ExistingId_DeletesBook")
    void deleteById_ExistingId_DeletesBook() {
        Book book = TestDataHelper.createBook();
        Book saved = bookRepository.save(book);
        Long bookId = saved.getId();
        bookRepository.deleteById(bookId);
        Optional<Book> found = bookRepository.findById(bookId);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("existsById_ExistingId_ReturnsTrue")
    void existsById_ExistingId_ReturnsTrue() {
        Book book = TestDataHelper.createBook();
        Book saved = bookRepository.save(book);
        boolean exists = bookRepository.existsById(saved.getId());
        assertTrue(exists);
    }

    @Test
    @DisplayName("existsById_NonExistingId_ReturnsFalse")
    void existsById_NonExistingId_ReturnsFalse() {
        boolean exists = bookRepository.existsById(999L);
        assertFalse(exists);
    }

    @Test
    @DisplayName("findAllByCategories_Id_WithNonMatchingCategory_ReturnsEmpty")
    void findAllByCategories_Id_WithNonMatchingCategory_ReturnsEmpty() {
        Book book = TestDataHelper.createBook();
        bookRepository.save(book);
        var books = bookRepository.findAllByCategories_Id(999L);
        assertTrue(books.isEmpty());
    }
}
