package org.example.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.dto.book.CreateBookRequestDto;
import org.example.helpers.TestDataHelper;
import org.example.model.Book;
import org.example.model.Category;
import org.example.repositories.BookRepository;
import org.example.repositories.CategoryRepository;
import org.example.services.user.impl.CustomUserDetailsService;
import org.example.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    void getBookById_ValidId_Returns200() throws Exception {
        Book book = bookRepository.save(
                TestDataHelper.createBook()
        );
        mockMvc.perform(get("/books/{id}", book.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_ValidRequest_Returns201() throws Exception {
        Category category = categoryRepository.save(
                TestDataHelper.createCategory()
        );
        CreateBookRequestDto request =
                TestDataHelper.createBookRequestDto(category.getId());
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
        assertEquals(1, bookRepository.count());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_ValidId_Returns204() throws Exception {
        Book book = bookRepository.save(
                TestDataHelper.createBook()
        );
        mockMvc.perform(delete("/books/{id}", book.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBookById_NonExistingId_Returns404() throws Exception {
        mockMvc.perform(get("/books/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_InvalidRequest_Returns400() throws Exception {
        Category category = categoryRepository.save(
                TestDataHelper.createCategory()
        );
        CreateBookRequestDto request =
                TestDataHelper.createBookRequestDto(category.getId());
        request.setTitle("");
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_InvalidCategoryId_Returns404() throws Exception {
        CreateBookRequestDto request =
                TestDataHelper.createBookRequestDto(999L);
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
