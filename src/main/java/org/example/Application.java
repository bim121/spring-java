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
            newBook.setIsbn("3576789890");
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
        };
    }
}
