package org.example.services.category.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dto.book.BookDtoWithoutCategoryIds;
import org.example.dto.category.CategoryDto;
import org.example.dto.category.CategoryRequestDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.BookMapper;
import org.example.mappers.CategoryMapper;
import org.example.model.Category;
import org.example.repositories.BookRepository;
import org.example.repositories.CategoryRepository;
import org.example.services.category.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Category not found: " + id))
        );
    }

    @Override
    public CategoryDto save(CategoryRequestDto dto) {
        return categoryMapper.toDto(
                categoryRepository.save(categoryMapper.toEntity(dto))
        );
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found: " + id)
                );
        categoryMapper.updateCategoryFromDto(dto, category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return bookRepository.findAllByCategories_Id(id).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    @Override
    public Set<Category> getCategoriesByIds(Set<Long> categoryIds) {
        return categoryIds.stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Category not found: " + id)))
                .collect(Collectors.toSet());
    }
}
