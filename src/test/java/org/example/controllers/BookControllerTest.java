package org.example.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import org.example.dto.book.BookDto;
import org.example.dto.book.CreateBookRequestDto;
import org.example.services.book.BookService;
import org.example.services.user.impl.CustomUserDetailsService;
import org.example.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("getBookById_ValidId_Returns200")
    void getBookById_ValidId_Returns200() throws Exception {
        BookDto dto = new BookDto();
        dto.setId(1L);
        when(bookService.getById(1L)).thenReturn(dto);
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("createBook_ValidRequest_Returns200")
    void createBook_ValidRequest_Returns200() throws Exception {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Test Book");
        request.setAuthor("Test Author");
        request.setIsbn("978-3-16-148410-0");
        request.setPrice(BigDecimal.valueOf(19.99));
        request.setCategoryIds(Set.of(1L));
        BookDto dto = new BookDto();
        when(bookService.create(any())).thenReturn(dto);
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("delete_ValidId_Returns200")
    void delete_ValidId_Returns200() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().isNoContent());
        verify(bookService).delete(1L);
    }
}
