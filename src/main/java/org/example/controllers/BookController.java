package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.book.BookDto;
import org.example.dto.book.CreateBookRequestDto;
import org.example.services.book.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Tag(name = "Books", description = "Book management API")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @Operation(summary = "Get all books with pagination and sorting")
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public Page<BookDto> getAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable
    ) {
        return bookService.getAll(pageable);
    }

    @Operation(summary = "Get book by id")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto book = bookService.getById(id);
        if (book != null) {
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new book")
    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody CreateBookRequestDto bookDto) {
        BookDto created = bookService.create(bookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update existing book")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto update(
            @PathVariable Long id,
            @RequestBody CreateBookRequestDto dto
    ) {
        return bookService.update(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete book by id (soft delete)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id);
    }
}
