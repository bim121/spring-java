package org.example.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dto.book.BookDtoWithoutCategoryIds;
import org.example.dto.category.CategoryDto;
import org.example.services.category.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto dto) {
        return categoryService.save(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<CategoryDto> getAll(@PageableDefault Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto update(@PathVariable Long id,
                              @Valid @RequestBody CategoryDto dto) {
        return categoryService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooks(@PathVariable Long id) {
        return categoryService.getBooksByCategoryId(id);
    }
}
