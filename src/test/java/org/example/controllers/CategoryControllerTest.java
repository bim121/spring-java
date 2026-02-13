package org.example.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.example.dto.category.CategoryDto;
import org.example.dto.category.CategoryRequestDto;
import org.example.services.category.CategoryService;
import org.example.services.user.impl.CustomUserDetailsService;
import org.example.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("getAll_Returns200")
    void getAll_Returns200() throws Exception {
        when(categoryService.findAll(any()))
                .thenReturn(Page.empty());
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("createCategory_ValidRequest_Returns200")
    void createCategory_ValidRequest_Returns201() throws Exception {
        CategoryRequestDto request = new CategoryRequestDto();
        CategoryDto dto = new CategoryDto();
        request.setName("Fiction");
        when(categoryService.save(any())).thenReturn(dto);
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
        when(categoryService.getBooksByCategoryId(1L))
                .thenReturn(List.of());
        mockMvc.perform(get("/categories/1/books"))
                .andExpect(status().isOk());
    }
}
