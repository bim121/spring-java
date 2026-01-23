package org.example.services;

import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.example.model.Book;
import java.util.List;

public interface BookService {
    List<BookDto> getAll();
    BookDto getById(Long id);
    BookDto create(CreateBookRequestDto requestDto);
}