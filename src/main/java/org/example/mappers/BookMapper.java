package org.example.mappers;

import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.example.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto requestDto);

    void updateBookFromDto(
            CreateBookRequestDto requestDto,
            @MappingTarget Book book
    );
}
