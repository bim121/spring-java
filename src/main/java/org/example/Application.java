package org.example;

import java.math.BigDecimal;
import java.util.List;
import org.example.model.Book;
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
            System.out.println("save book");
            Book book = new Book();
            book.setTitle("Sherlock Holmes");
            book.setAuthor("Konan Doyle");
            book.setIsbn("3576789890");
            book.setPrice(new BigDecimal("29.99"));
            book.setDescription("awesome");
            book.setCoverImage("https://sherlock.jpg");
            bookService.save(book);
            System.out.println(book.getTitle());
            System.out.println("get all");
            List<Book> books = bookService.findAll();
            for (Book b: books) {
                System.out.println(b.getTitle());
            }
        };
    }
}
