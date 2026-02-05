package org.example.mappers;

import java.util.stream.Collectors;
import org.example.dto.book.BookDto;
import org.example.dto.book.BookDtoWithoutCategoryIds;
import org.example.dto.book.CreateBookRequestDto;
import org.example.model.Book;
import org.example.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    Book toEntity(CreateBookRequestDto requestDto);

    void updateBookFromDto(
            CreateBookRequestDto requestDto,
            @MappingTarget Book book
    );

    @AfterMapping
    default void setCategoryIds(
            @MappingTarget BookDto bookDto,
            Book book
    ) {
        if (book.getCategories() != null) {
            bookDto.setCategoryIds(
                    book.getCategories().stream()
                            .map(Category::getId)
                            .collect(Collectors.toSet())
            );
        }
    }
}
