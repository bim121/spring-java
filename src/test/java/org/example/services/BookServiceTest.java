package org.example.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.example.dto.book.BookDto;
import org.example.dto.book.CreateBookRequestDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.helpers.TestDataHelper;
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
        final Long id = 1L;
        Book book = TestDataHelper.createBookWithDetails(
                "Test Book", "Test Author", "1234567890",
                new BigDecimal("19.99"), "Test Description",
                "https://example/cover.jpg"
        );
        book.setId(id);
        BookDto expectedDto = TestDataHelper.createBookDtoWithDetails(
                id, "Test Book", "Test Author", "1234567890",
                new BigDecimal("19.99"), "Test Description",
                "https://example/cover.jpg"
        );
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedDto);
        BookDto result = bookService.getById(id);
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getTitle(), result.getTitle());
        assertEquals(expectedDto.getAuthor(), result.getAuthor());
        assertEquals(expectedDto.getIsbn(), result.getIsbn());
        assertEquals(expectedDto.getPrice(), result.getPrice());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        assertEquals(expectedDto.getCoverImage(), result.getCoverImage());
        verify(bookRepository).findById(id);
    }

    @Test
    @DisplayName("Given valid CreateBookRequestDto, create should save and return BookDto")
    void create_ValidRequest_ReturnsBookDto() {
        CreateBookRequestDto request = TestDataHelper.createBookRequestDtoWithDetails(
                "New Book", "New Author", "9876543210",
                new BigDecimal("29.99"), "New Description",
                "https://example/new-cover.jpg", Set.of(1L)
        );
        Book book = TestDataHelper.createBookWithDetails(
                "New Book", "New Author", "9876543210",
                new BigDecimal("29.99"), "New Description",
                "https://example/new-cover.jpg"
        );
        BookDto expectedDto = TestDataHelper.createBookDtoWithDetails(
                1L, "New Book", "New Author", "9876543210",
                new BigDecimal("29.99"), "New Description",
                "https://example/new-cover.jpg"
        );
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(categoryService.getCategoriesByIds(any())).thenReturn(Set.of());
        when(bookMapper.toDto(book)).thenReturn(expectedDto);
        BookDto result = bookService.create(request);
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getTitle(), result.getTitle());
        assertEquals(expectedDto.getAuthor(), result.getAuthor());
        assertEquals(expectedDto.getIsbn(), result.getIsbn());
        assertEquals(expectedDto.getPrice(), result.getPrice());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        assertEquals(expectedDto.getCoverImage(), result.getCoverImage());
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("Given existing book id and valid request, update should return updated BookDto")
    void update_ExistingId_ReturnsUpdatedBookDto() {
        final Long id = 1L;
        final CreateBookRequestDto request = TestDataHelper.createBookRequestDtoWithDetails(
                "Updated Book", "Updated Author", "1111111111",
                new BigDecimal("39.99"), "Updated Description",
                "https://example/updated-cover.jpg", Set.of(1L)
        );
        Book existingBook = TestDataHelper.createBookWithDetails(
                "Old Book", "Old Author", "0000000000",
                new BigDecimal("19.99"), null, null
        );
        existingBook.setId(id);
        BookDto expectedDto = TestDataHelper.createBookDtoWithDetails(
                id, "Updated Book", "Updated Author", "1111111111",
                new BigDecimal("39.99"), "Updated Description",
                "https://example/updated-cover.jpg"
        );
        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(categoryService.getCategoriesByIds(any())).thenReturn(Set.of());
        when(bookMapper.toDto(existingBook)).thenReturn(expectedDto);
        BookDto result = bookService.update(id, request);
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getTitle(), result.getTitle());
        assertEquals(expectedDto.getAuthor(), result.getAuthor());
        assertEquals(expectedDto.getIsbn(), result.getIsbn());
        assertEquals(expectedDto.getPrice(), result.getPrice());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        assertEquals(expectedDto.getCoverImage(), result.getCoverImage());
        verify(bookRepository).findById(id);
        verify(categoryService).getCategoriesByIds(any());
    }

    @Test
    @DisplayName("Given existing book id, delete should call repository delete")
    void delete_ExistingId_CallsRepositoryDelete() {
        Book book = TestDataHelper.createBook();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        bookService.delete(1L);
        verify(bookRepository).delete(book);
    }

    @Test
    @DisplayName("Given non-existing book id, getById should throw EntityNotFoundException")
    void getById_NonExistingId_ThrowsEntityNotFoundException() {
        Long id = 999L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.getById(id));
        verify(bookRepository).findById(id);
    }

    @Test
    @DisplayName("Given non-existing book id, update should throw EntityNotFoundException")
    void update_NonExistingId_ThrowsEntityNotFoundException() {
        Long id = 999L;
        CreateBookRequestDto request = new CreateBookRequestDto();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.update(id, request));
        verify(bookRepository).findById(id);
    }

    @Test
    @DisplayName("Given non-existing book id, delete should throw EntityNotFoundException")
    void delete_NonExistingId_ThrowsEntityNotFoundException() {
        Long id = 999L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.delete(id));
        verify(bookRepository).findById(id);
    }

    @Test
    @DisplayName("Given non-existing category ids, create should throw EntityNotFoundException")
    void create_NonExistingCategoryIds_ThrowsEntityNotFoundException() {
        CreateBookRequestDto request = TestDataHelper.createBookRequestDto(999L);
        Book book = TestDataHelper.createBook();
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(categoryService.getCategoriesByIds(any()))
                .thenThrow(new EntityNotFoundException("Category not found: 999"));
        assertThrows(EntityNotFoundException.class, () -> bookService.create(request));
        verify(categoryService).getCategoriesByIds(any());
    }

    @Test
    @DisplayName("Given non-existing category ids, update should throw EntityNotFoundException")
    void update_NonExistingCategoryIds_ThrowsEntityNotFoundException() {
        final Long bookId = 1L;
        CreateBookRequestDto request = TestDataHelper.createBookRequestDto(999L);
        Book book = TestDataHelper.createBook();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(categoryService.getCategoriesByIds(any()))
                .thenThrow(new EntityNotFoundException("Category not found: 999"));
        assertThrows(EntityNotFoundException.class, () -> bookService.update(bookId, request));
        verify(bookRepository).findById(bookId);
        verify(categoryService).getCategoriesByIds(any());
    }
}
