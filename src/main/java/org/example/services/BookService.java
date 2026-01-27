package org.example.services;

import java.util.List;
import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;

public interface BookService {
    List<BookDto> getAll();
    
    BookDto getById(Long id);

    BookDto create(CreateBookRequestDto requestDto);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);
}
