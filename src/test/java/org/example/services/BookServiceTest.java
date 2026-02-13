package org.example.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import org.example.dto.book.BookDto;
import org.example.dto.book.CreateBookRequestDto;
import org.example.mappers.BookMapper;
import org.example.model.Book;
import org.example.repositories.BookRepository;
import org.example.services.book.impl.BookServiceImpl;
import org.example.services.category.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Given existing book id, getById should return BookDto")
    void getById_ExistingId_ReturnsBookDto() {
        Long id = 1L;
        Book book = new Book();
        BookDto dto = new BookDto();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(dto);
        BookDto result = bookService.getById(id);
        assertNotNull(result);
        verify(bookRepository).findById(id);
    }

    @Test
    @DisplayName("Given valid CreateBookRequestDto, create should save and return BookDto")
    void create_ValidRequest_ReturnsBookDto() {
        CreateBookRequestDto request = new CreateBookRequestDto();
        Book book = new Book();
        BookDto dto = new BookDto();
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(categoryService.getCategoriesByIds(any())).thenReturn(Set.of());
        when(bookMapper.toDto(book)).thenReturn(dto);
        BookDto result = bookService.create(request);
        assertNotNull(result);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("Given existing book id, delete should call repository delete")
    void delete_ExistingId_CallsRepositoryDelete() {
        Book book = new Book();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.delete(1L);
        verify(bookRepository).delete(book);
    }
}
