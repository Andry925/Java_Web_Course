package org.example.galacticmarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galacticmarket.dto.CategoryDTO;
import org.example.galacticmarket.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private ObjectMapper objectMapper;
    private CategoryDTO mockCategory;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockCategory = CategoryDTO.builder()
                .id(UUID.randomUUID())
                .name("Dairy")
                .build();
    }

    @Test
    void getAllCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(mockCategory));

        mockMvc.perform(get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Dairy"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById() throws Exception {
        UUID categoryId = mockCategory.getId();
        when(categoryService.getCategoryById(categoryId)).thenReturn(mockCategory);

        mockMvc.perform(get("/api/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dairy"));

        verify(categoryService, times(1)).getCategoryById(categoryId);
    }

    @Test
    void createCategory() throws Exception {
        when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(mockCategory);

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dairy"));

        verify(categoryService, times(1)).createCategory(any(CategoryDTO.class));
    }

    @Test
    void updateCategory() throws Exception {
        UUID categoryId = mockCategory.getId();
        when(categoryService.updateCategory(eq(categoryId), any(CategoryDTO.class))).thenReturn(mockCategory);

        mockMvc.perform(put("/api/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dairy"));

        verify(categoryService, times(1)).updateCategory(eq(categoryId), any(CategoryDTO.class));
    }


    @Test
    void deleteCategory() throws Exception {
        UUID categoryId = mockCategory.getId();
        doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(categoryId);
    }


}