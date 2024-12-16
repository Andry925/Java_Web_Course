package org.example.galacticmarket.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galacticmarket.dto.CategoryDTO;
import org.example.galacticmarket.repository.CategoryRepository;
import org.example.galacticmarket.repository.entity.CategoryEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class CategoryControllerIntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.8-alpine3.18")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withEnv("TZ", "UTC");

    @BeforeAll
    static void startContainer() {
        postgres.start();
    }

    @AfterAll
    static void stopContainer() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureTestDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();
    }

    @Test
    void testCreateCategoryValid() throws Exception {
        CategoryDTO validCategory = CategoryDTO.builder()
                .name("Dairy")
                .build();

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dairy"));
    }

    @Test
    void testCreateCategoryInvalid() throws Exception {
        CategoryDTO invalidCategory = CategoryDTO.builder()
                .name("")
                .build();

        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void testGetAllCategories() throws Exception {
        CategoryEntity category1 = categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .name("Dairy")
                .build());

        CategoryEntity category2 = categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .name("Beverages")
                .build());

        mockMvc.perform(get("/api/v1/categories")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(category1.getName()))
                .andExpect(jsonPath("$[1].name").value(category2.getName()));
    }

    @Test
    void testGetCategoryById() throws Exception {
        CategoryEntity category = categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .name("Dairy")
                .build());

        mockMvc.perform(get("/api/v1/categories/{id}", category.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dairy"));
    }

    @Test
    void testUpdateCategoryValid() throws Exception {
        CategoryEntity category = categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .name("Dairy")
                .build());

        CategoryDTO updatedCategory = CategoryDTO.builder()
                .name("Updated Dairy")
                .build();

        mockMvc.perform(put("/api/v1/categories/{id}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Dairy"));
    }

    @Test
    void testUpdateCategoryInvalid() throws Exception {
        CategoryEntity category = categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .name("Dairy")
                .build());

        CategoryDTO invalidCategory = CategoryDTO.builder()
                .name("") // Invalid name
                .build();

        mockMvc.perform(put("/api/v1/categories/{id}", category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteCategory() throws Exception {
        CategoryEntity category = categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .name("Dairy")
                .build());

        mockMvc.perform(delete("/api/v1/categories/{id}", category.getId()))
                .andExpect(status().isNoContent());

        assertFalse(categoryRepository.findById(category.getId()).isPresent());
    }
}
