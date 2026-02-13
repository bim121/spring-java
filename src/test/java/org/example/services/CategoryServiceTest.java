package org.example.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.dto.book.BookDtoWithoutCategoryIds;
import org.example.dto.category.CategoryDto;
import org.example.dto.category.CategoryRequestDto;
import org.example.mappers.BookMapper;
import org.example.mappers.CategoryMapper;
import org.example.model.Book;
import org.example.model.Category;
import org.example.repositories.BookRepository;
import org.example.repositories.CategoryRepository;
import org.example.services.category.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("getById_ExistingId_ReturnsCategoryDto")
    void getById_ExistingId_ReturnsCategoryDto() {
        Category category = new Category();
        CategoryDto dto = new CategoryDto();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(dto);
        CategoryDto result = categoryService.getById(1L);
        assertNotNull(result);
    }

    @Test
    @DisplayName("save_ValidDto_ReturnsSavedCategory")
    void save_ValidDto_ReturnsSavedCategory() {
        CategoryRequestDto request = new CategoryRequestDto();
        Category category = new Category();
        CategoryDto dto = new CategoryDto();
        when(categoryMapper.toEntity(request)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(dto);
        CategoryDto result = categoryService.save(request);
        assertNotNull(result);
    }

    @Test
    @DisplayName("getBooksByCategoryId_ReturnsBooks")
    void getBooksByCategoryId_ReturnsBooks() {
        Book book = new Book();
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        when(bookRepository.findAllByCategories_Id(1L)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dto);
        List<BookDtoWithoutCategoryIds> result =
                categoryService.getBooksByCategoryId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void update_ExistingId_ReturnsUpdatedDto() {
        Long id = 1L;
        Category category = new Category();
        CategoryRequestDto request = new CategoryRequestDto();
        CategoryDto dto = new CategoryDto();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(dto);
        CategoryDto result = categoryService.update(id, request);
        assertNotNull(result);
    }

    @Test
    @DisplayName("deleteById_ValidId_CallsRepository")
    void deleteById_ValidId_CallsRepository() {
        categoryService.deleteById(1L);
        verify(categoryRepository).deleteById(1L);
    }
}
