package org.example;

import java.math.BigDecimal;
import java.util.List;
import org.example.dto.BookDto;
import org.example.dto.CreateBookRequestDto;
import org.example.services.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
            newBook.setIsbn("3587678788890");
            newBook.setPrice(new BigDecimal("29.99"));
            newBook.setDescription("awesome");
            newBook.setCoverImage("https://sherlock.jpg");
            BookDto savedBook = bookService.create(newBook);
            System.out.println("save book: " + savedBook.getTitle());
            System.out.println("get all books");
            List<BookDto> books = bookService.getAll();
            for (BookDto b : books) {
                System.out.println("book: " + b.getTitle() + " | author: " + b.getAuthor());
            }
            System.out.println("get book by id");
            Long id = savedBook.getId();
            BookDto bookById = bookService.getById(id);
            if (bookById != null) {
                System.out.println("find: " + bookById.getTitle());
            } else {
                System.out.println("don't find book with id " + id);
            }
            System.out.println("\nUpdated book");
            CreateBookRequestDto updateDto = new CreateBookRequestDto();
            updateDto.setTitle("Sherlock Holmes (Updated)");
            updateDto.setAuthor("Arthur Conan Doyle");
            updateDto.setIsbn("35768557679890");
            updateDto.setPrice(new BigDecimal("19.99"));
            updateDto.setDescription("updated description");
            updateDto.setCoverImage("https://sherlock-updated.jpg");
            BookDto updatedBook = bookService.update(id, updateDto);
            System.out.println("Updated title: " + updatedBook.getTitle());
            System.out.println("Updated price: " + updatedBook.getPrice());
            System.out.println("\ndelete book(soft delete)");
            bookService.delete(id);
            System.out.println("Book deleted (soft)");
            System.out.println("\ntry get deleted book");
            try {
                bookService.getById(id);
                System.out.println("error: deleted book is still accessible");
            } catch (Exception e) {
                System.out.println("Deleted book not found (good!)");
            }
            System.out.println("\nget all book after delete");
            List<BookDto> booksAfterDelete = bookService.getAll();
            if (booksAfterDelete.isEmpty()) {
                System.out.println("No books returned (soft delete works)");
            } else {
                booksAfterDelete.forEach(b ->
                        System.out.println(b.getId() + " | " + b.getTitle())
                );
            }
        };
    }
}
