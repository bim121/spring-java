package org.example;

import java.math.BigDecimal;
import org.example.dto.book.BookDto;
import org.example.dto.book.CreateBookRequestDto;
import org.example.dto.user.UserRegistrationRequestDto;
import org.example.dto.user.UserResponseDto;
import org.example.exceptions.RegistrationException;
import org.example.services.auth.AuthenticationService;
import org.example.services.book.BookService;
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
    public CommandLineRunner commandLineRunner(BookService bookService,
                                               AuthenticationService authenticationService) {
        return args -> {

            System.out.println("create book");
            CreateBookRequestDto newBook = new CreateBookRequestDto();
            newBook.setTitle("Sherlock Holmes");
            newBook.setAuthor("Konan Doyle");
            newBook.setIsbn("3587178585544890");
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
            updateDto.setIsbn("3576837637938490");
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
            System.out.println("\nuser registration service test");
            UserRegistrationRequestDto userDto = new UserRegistrationRequestDto();
            userDto.setEmail("john.doe@example.com");
            userDto.setPassword("12345678");
            userDto.setRepeatPassword("12345678");
            userDto.setFirstName("John");
            userDto.setLastName("Doe");
            userDto.setShippingAddress("123 Main St, City, Country");
            try {
                UserResponseDto savedUser = authenticationService.register(userDto);
                System.out.println("User registered successfully: "
                        + savedUser.getEmail()
                        + " | ID: "
                        + savedUser.getId());
            } catch (RegistrationException e) {
                System.out.println("Registration failed: " + e.getMessage());
            }
            try {
                authenticationService.register(userDto);
            } catch (RegistrationException e) {
                System.out.println("Duplicate email registration correctly failed: "
                        + e.getMessage());
            }
            UserRegistrationRequestDto userWrongPassword = new UserRegistrationRequestDto();
            userWrongPassword.setEmail("jane.doe@example.com");
            userWrongPassword.setPassword("password1");
            userWrongPassword.setRepeatPassword("password2");
            userWrongPassword.setFirstName("Jane");
            userWrongPassword.setLastName("Doe");
            userWrongPassword.setShippingAddress("456 Main St, City, Country");
            try {
                authenticationService.register(userWrongPassword);
            } catch (RegistrationException e) {
                System.out.println("Password mismatch correctly failed: " + e.getMessage());
            }
        };
    }
}
