package org.example.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.dto.category.CategoryRequestDto;
import org.example.helpers.TestDataHelper;
import org.example.model.Book;
import org.example.model.Category;
import org.example.repositories.BookRepository;
import org.example.repositories.CategoryRepository;
import org.example.services.user.impl.CustomUserDetailsService;
import org.example.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
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
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_Returns200() throws Exception {
        categoryRepository.save(TestDataHelper.createCategory());
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_ValidRequest_Returns201() throws Exception {
        CategoryRequestDto request = new CategoryRequestDto();
        request.setName("Fiction");
        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("getBooks_ValidCategory_Returns200")
    void getBooks_ValidCategory_Returns200() throws Exception {
        Category category = categoryRepository.save(TestDataHelper.createCategory());
        Book book = TestDataHelper.createBook();
        book.getCategories().add(category);
        bookRepository.save(book);
        mockMvc.perform(get("/categories/{id}/books", category.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_InvalidRequest_Returns400() throws Exception {
        CategoryRequestDto request = new CategoryRequestDto();
        request.setName("");
        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBooks_NonExistingCategory_Returns404() throws Exception {
        mockMvc.perform(get("/categories/999/books"))
                .andExpect(status().isNotFound());
    }
}
