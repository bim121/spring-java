package org.example.mappers;

import org.example.dto.category.CategoryDto;
import org.example.dto.category.CategoryRequestDto;
import org.example.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toEntity(CategoryRequestDto dto);

    void updateCategoryFromDto(CategoryRequestDto dto, @MappingTarget Category category);
}
