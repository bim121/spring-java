package org.example.helpers;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import org.example.dto.book.BookDto;
import org.example.dto.book.BookDtoWithoutCategoryIds;
import org.example.dto.book.CreateBookRequestDto;
import org.example.dto.category.CategoryDto;
import org.example.dto.category.CategoryRequestDto;
import org.example.model.Book;
import org.example.model.Category;

public class TestDataHelper {
    public static Category createCategory() {
        Category category = new Category();
        category.setName("Fiction");
        return category;
    }

    public static Book createBook() {
        Book book = new Book();
        book.setTitle("Test");
        book.setAuthor("Author");
        book.setIsbn(UUID.randomUUID().toString());
        book.setPrice(new BigDecimal("19.99"));
        return book;
    }

    public static Book createBookWithId(Long id) {
        Book book = createBook();
        book.setId(id);
        return book;
    }

    public static Book createBookWithDetails(String title, String author, String isbn,
                                             BigDecimal price, String description,
                                             String coverImage
    ) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPrice(price);
        book.setDescription(description);
        book.setCoverImage(coverImage);
        return book;
    }

    public static BookDto createBookDto() {
        BookDto dto = new BookDto();
        dto.setTitle("Test");
        dto.setAuthor("Author");
        dto.setIsbn("1234567890");
        dto.setPrice(new BigDecimal("19.99"));
        return dto;
    }

    public static BookDto createBookDtoWithId(Long id) {
        BookDto dto = createBookDto();
        dto.setId(id);
        return dto;
    }

    public static BookDto createBookDtoWithDetails(Long id, String title, String author, 
                                                   String isbn, BigDecimal price, 
                                                   String description, String coverImage
    ) {
        BookDto dto = new BookDto();
        dto.setId(id);
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setIsbn(isbn);
        dto.setPrice(price);
        dto.setDescription(description);
        dto.setCoverImage(coverImage);
        return dto;
    }

    public static CreateBookRequestDto createBookRequestDto(Long categoryId) {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Test Book");
        request.setAuthor("Test Author");
        request.setIsbn("978-3-16-148410-0");
        request.setPrice(BigDecimal.valueOf(19.99));
        request.setCategoryIds(Set.of(categoryId));
        return request;
    }

    public static CreateBookRequestDto createBookRequestDtoWithDetails(String title,
                                                                       String author, String isbn,
                                                                       BigDecimal price,
                                                                       String description,
                                                                       String coverImage,
                                                                       Set<Long> categoryIds
    ) {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle(title);
        request.setAuthor(author);
        request.setIsbn(isbn);
        request.setPrice(price);
        request.setDescription(description);
        request.setCoverImage(coverImage);
        request.setCategoryIds(categoryIds);
        return request;
    }

    public static Category createCategoryWithDetails(String name,
                                                     String description
    ) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setDeleted(false);
        return category;
    }

    public static Category createCategoryWithId(Long id) {
        Category category = createCategory();
        category.setId(id);
        return category;
    }

    public static Category createCategoryWithIdAndDetails(Long id,
                                                          String name,
                                                          String description
    ) {
        Category category = createCategoryWithDetails(name, description);
        category.setId(id);
        return category;
    }

    public static CategoryDto createCategoryDto() {
        CategoryDto dto = new CategoryDto();
        dto.setName("Fiction");
        return dto;
    }

    public static CategoryDto createCategoryDtoWithId(Long id) {
        CategoryDto dto = createCategoryDto();
        dto.setId(id);
        return dto;
    }

    public static CategoryDto createCategoryDtoWithDetails(Long id,
                                                           String name,
                                                           String description
    ) {
        CategoryDto dto = new CategoryDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        return dto;
    }

    public static CategoryRequestDto createCategoryRequestDto(String name,
                                                              String description
    ) {
        CategoryRequestDto request = new CategoryRequestDto();
        request.setName(name);
        request.setDescription(description);
        return request;
    }

    public static BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setId(1L);
        dto.setTitle("Test Book");
        dto.setAuthor("Test Author");
        dto.setPrice(new BigDecimal("19.99"));
        return dto;
    }
}
