package org.example.services.category;

import java.util.List;
import java.util.Set;
import org.example.dto.book.BookDtoWithoutCategoryIds;
import org.example.dto.category.CategoryDto;
import org.example.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto dto);

    CategoryDto update(Long id, CategoryDto dto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id);

    Set<Category> getCategoriesByIds(Set<Long> categoryIds);
}
