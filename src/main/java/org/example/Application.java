package org.example;

import java.math.BigDecimal;
import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.example.services.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {

            System.out.println("create book");
            CreateBookRequestDto newBook = new CreateBookRequestDto();
            newBook.setTitle("Sherlock Holmes");
            newBook.setAuthor("Konan Doyle");
            newBook.setIsbn("3587678785855890");
            newBook.setPrice(new BigDecimal("29.99"));
            newBook.setDescription("awesome");
            newBook.setCoverImage("https://sherlock.jpg");
            BookDto savedBook = bookService.create(newBook);
            System.out.println("save book: " + savedBook.getTitle());
            Pageable pageable = PageRequest.of(
                    0,
                    10,
                    Sort.by("title").ascending()
            );
            System.out.println("\nget all books (page 0)");
            Page<BookDto> bookPage = bookService.getAll(pageable);
            for (BookDto b : bookPage.getContent()) {
                System.out.println("book: " + b.getTitle() + " | author: " + b.getAuthor());
            }
            System.out.println("Total elements: " + bookPage.getTotalElements());
            System.out.println("Total pages: " + bookPage.getTotalPages());
            System.out.println("Is last page: " + bookPage.isLast());
            System.out.println("\nget book by id");
            Long id = savedBook.getId();
            BookDto bookById = bookService.getById(id);
            System.out.println("find: " + bookById.getTitle());
            System.out.println("\nUpdated book");
            CreateBookRequestDto updateDto = new CreateBookRequestDto();
            updateDto.setTitle("Sherlock Holmes (Updated)");
            updateDto.setAuthor("Arthur Conan Doyle");
            updateDto.setIsbn("3576855767938490");
            updateDto.setPrice(new BigDecimal("19.99"));
            updateDto.setDescription("updated description");
            updateDto.setCoverImage("https://sherlock-updated.jpg");
            BookDto updatedBook = bookService.update(id, updateDto);
            System.out.println("Updated title: " + updatedBook.getTitle());
            System.out.println("Updated price: " + updatedBook.getPrice());
            System.out.println("\ndelete book (soft delete)");
            bookService.delete(id);
            System.out.println("\ntry get deleted book");
            try {
                bookService.getById(id);
                System.out.println("error: deleted book is still accessible");
            } catch (Exception e) {
                System.out.println("Deleted book not found (good!)");
            }
            System.out.println("\nget all books after delete");
            Page<BookDto> pageAfterDelete = bookService.getAll(pageable);
            if (pageAfterDelete.isEmpty()) {
                System.out.println("No books returned (soft delete works)");
            } else {
                pageAfterDelete.getContent().forEach(b ->
                        System.out.println(b.getId() + " | " + b.getTitle())
                );
            }
        };
    }
}
