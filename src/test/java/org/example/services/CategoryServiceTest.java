package org.example.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.dto.book.BookDtoWithoutCategoryIds;
import org.example.dto.category.CategoryDto;
import org.example.dto.category.CategoryRequestDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.helpers.TestDataHelper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    @DisplayName("Given existing category id, getById should return CategoryDto")
    void getById_ExistingId_ReturnsCategoryDto() {
        final Long id = 1L;
        Category category = TestDataHelper.createCategoryWithIdAndDetails(
                id, "Fiction", "Fiction books category"
        );
        CategoryDto expectedDto = TestDataHelper.createCategoryDtoWithDetails(
                id, "Fiction", "Fiction books category"
        );
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);
        CategoryDto result = categoryService.getById(id);
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        verify(categoryRepository).findById(id);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Given valid CategoryRequestDto, save should save and return CategoryDto")
    void save_ValidRequest_ReturnsSavedCategory() {
        CategoryRequestDto request = TestDataHelper.createCategoryRequestDto(
                "Science Fiction", "Science fiction books category"
        );
        Category category = TestDataHelper.createCategoryWithDetails(
                "Science Fiction", "Science fiction books category"
        );
        CategoryDto expectedDto = TestDataHelper.createCategoryDtoWithDetails(
                1L, "Science Fiction", "Science fiction books category"
        );
        when(categoryMapper.toEntity(request)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);
        CategoryDto result = categoryService.save(request);
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        verify(categoryMapper).toEntity(request);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Given existing category id, "
            + "getBooksByCategoryId should return list of BookDtoWithoutCategoryIds")
    void getBooksByCategoryId_ExistingId_ReturnsBooks() {
        final Long categoryId = 1L;
        Category category = TestDataHelper.createCategoryWithIdAndDetails(
                categoryId, "Fiction", "Fiction books category"
        );
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Book book = TestDataHelper.createBookWithId(1L);
        BookDtoWithoutCategoryIds expectedDto = TestDataHelper.createBookDtoWithoutCategoryIds();
        when(bookRepository.findAllByCategories_Id(categoryId)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(expectedDto);
        List<BookDtoWithoutCategoryIds> result = categoryService.getBooksByCategoryId(categoryId);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDto.getId(), result.get(0).getId());
        assertEquals(expectedDto.getTitle(), result.get(0).getTitle());
        assertEquals(expectedDto.getAuthor(), result.get(0).getAuthor());
        assertEquals(expectedDto.getPrice(), result.get(0).getPrice());
        verify(categoryRepository).findById(categoryId);
        verify(bookRepository).findAllByCategories_Id(categoryId);
        verify(bookMapper).toDtoWithoutCategories(book);
    }

    @Test
    @DisplayName("Given existing category id and valid request, "
            + "update should return updated CategoryDto")
    void update_ExistingId_ReturnsUpdatedDto() {
        final Long id = 1L;
        CategoryRequestDto request = TestDataHelper.createCategoryRequestDto(
                "Updated Category", "Updated description"
        );
        Category existingCategory = TestDataHelper.createCategoryWithIdAndDetails(
                id, "Old Category", "Old description"
        );
        CategoryDto expectedDto = TestDataHelper.createCategoryDtoWithDetails(
                id, "Updated Category", "Updated description"
        );
        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryMapper.toDto(existingCategory)).thenReturn(expectedDto);
        CategoryDto result = categoryService.update(id, request);
        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        verify(categoryRepository).findById(id);
        verify(categoryMapper).toDto(existingCategory);
    }

    @Test
    @DisplayName("Given existing category id, deleteById should call repository deleteById")
    void deleteById_ExistingId_CallsRepositoryDelete() {
        final Long id = 1L;
        categoryService.deleteById(id);
        verify(categoryRepository).deleteById(id);
    }

    @Test
    @DisplayName("Given non-existing category id, getById should throw EntityNotFoundException")
    void getById_NonExistingId_ThrowsEntityNotFoundException() {
        Long id = 999L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(id));
        verify(categoryRepository).findById(id);
    }

    @Test
    @DisplayName("Given non-existing category id, update should throw EntityNotFoundException")
    void update_NonExistingId_ThrowsEntityNotFoundException() {
        Long id = 999L;
        CategoryRequestDto request = TestDataHelper.createCategoryRequestDto(
                "Test Category", "Test description"
        );
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.update(id, request));
        verify(categoryRepository).findById(id);
    }

    @Test
    @DisplayName("Given non-existing category id, "
            + "getBooksByCategoryId should throw EntityNotFoundException")
    void getBooksByCategoryId_NonExistingId_ThrowsEntityNotFoundException() {
        Long id = 999L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.getBooksByCategoryId(id));
        verify(categoryRepository).findById(id);
    }

    @Test
    @DisplayName("Given valid Pageable, findAll should return Page of CategoryDto")
    void findAll_ValidPageable_ReturnsPageOfCategoryDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Category category1 = TestDataHelper.createCategoryWithIdAndDetails(
                1L, "Fiction", "Fiction books"
        );
        Category category2 = TestDataHelper.createCategoryWithIdAndDetails(
                2L, "Science", "Science books"
        );
        CategoryDto dto1 = TestDataHelper.createCategoryDtoWithDetails(
                1L, "Fiction", "Fiction books"
        );
        CategoryDto dto2 = TestDataHelper.createCategoryDtoWithDetails(
                2L, "Science", "Science books"
        );
        Page<Category> categoryPage = new PageImpl<>(List.of(category1, category2), pageable, 2);
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category1)).thenReturn(dto1);
        when(categoryMapper.toDto(category2)).thenReturn(dto2);
        Page<CategoryDto> result = categoryService.findAll(pageable);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(dto1.getId(), result.getContent().get(0).getId());
        assertEquals(dto1.getName(), result.getContent().get(0).getName());
        assertEquals(dto1.getDescription(), result.getContent().get(0).getDescription());
        assertEquals(dto2.getId(), result.getContent().get(1).getId());
        assertEquals(dto2.getName(), result.getContent().get(1).getName());
        assertEquals(dto2.getDescription(), result.getContent().get(1).getDescription());
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Given valid category ids, getCategoriesByIds should return Set of Categories")
    void getCategoriesByIds_ValidIds_ReturnsSetOfCategories() {
        Set<Long> categoryIds = Set.of(1L, 2L);
        Category category1 = TestDataHelper.createCategoryWithIdAndDetails(
                1L, "Fiction", "Fiction books"
        );
        Category category2 = TestDataHelper.createCategoryWithIdAndDetails(
                2L, "Science", "Science books"
        );
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category2));
        Set<Category> result = categoryService.getCategoriesByIds(categoryIds);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertNotNull(result.stream().filter(c -> c.getId().equals(1L)).findFirst().orElse(null));
        assertNotNull(result.stream().filter(c -> c.getId().equals(2L)).findFirst().orElse(null));
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findById(2L);
    }

    @Test
    @DisplayName("Given non-existing category id in set, "
            + "getCategoriesByIds should throw EntityNotFoundException")
    void getCategoriesByIds_NonExistingId_ThrowsEntityNotFoundException() {
        Set<Long> categoryIds = Set.of(1L, 999L);
        Category category1 = TestDataHelper.createCategoryWithIdAndDetails(
                1L, "Fiction", "Fiction books"
        );
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, 
                () -> categoryService.getCategoriesByIds(categoryIds));
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).findById(999L);
    }
}
