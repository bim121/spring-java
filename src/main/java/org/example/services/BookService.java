package org.example.services;

import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<BookDto> getAll(Pageable pageable);
    
    BookDto getById(Long id);

    BookDto create(CreateBookRequestDto requestDto);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);
}
