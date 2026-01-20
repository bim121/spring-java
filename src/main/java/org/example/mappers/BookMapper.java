package org.example.mappers;

import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.example.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto toDto(Book book);

    Book toEntity(CreateBookRequestDto requestDto);
}
