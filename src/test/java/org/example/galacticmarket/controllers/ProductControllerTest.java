package org.example.galacticmarket.controllers;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galacticmarket.dto.ProductDTO;
import org.example.galacticmarket.mappers.ProductMapper;
import org.example.galacticmarket.repository.CategoryRepository;
import org.example.galacticmarket.repository.ProductRepository;
import org.example.galacticmarket.repository.entity.CategoryEntity;
import org.example.galacticmarket.repository.entity.ProductEntity;
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
class ProductControllerIntegrationTest {

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
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryEntity categoryEntity;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        categoryEntity = categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .name("Galactic Goods")
                .build());
    }

    @Test
    void testCreateProductValid() throws Exception {
        ProductDTO validProduct = ProductDTO.builder()
                .category_id(categoryEntity.getId())
                .name("Star Juice")
                .description("Refreshing intergalactic drink.")
                .galaxyOrigin("Andromeda")
                .price(12.99f)
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Star Juice"))
                .andExpect(jsonPath("$.galaxyOrigin").value("Andromeda"));
    }

    @Test
    void testCreateProductInvalid() throws Exception {
        ProductDTO invalidProduct = ProductDTO.builder()
                .category_id(categoryEntity.getId())
                .name("Juice")
                .description("")
                .galaxyOrigin("")
                .price(-5.0f)
                .build();

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void testGetAllProducts() throws Exception {
        ProductEntity product1 = productRepository.save(ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Star Juice")
                .description("Refreshing intergalactic drink.")
                .galaxyOrigin("Andromeda")
                .price(12.99f)
                .category(categoryEntity)
                .build());

        ProductEntity product2 = productRepository.save(ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Galactic Tea")
                .description("Relaxing tea from another galaxy.")
                .galaxyOrigin("Milky Way")
                .price(9.99f)
                .category(categoryEntity)
                .build());

        mockMvc.perform(get("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value(product1.getName()))
                .andExpect(jsonPath("$[1].name").value(product2.getName()));
    }

    @Test
    void testDeleteProduct() throws Exception {
        ProductEntity product = productRepository.save(ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Star Juice")
                .description("Refreshing intergalactic drink.")
                .galaxyOrigin("Andromeda")
                .price(12.99f)
                .category(categoryEntity)
                .build());

        mockMvc.perform(delete("/api/v1/products/{id}", product.getId()))
                .andExpect(status().isNoContent());

        assertFalse(productRepository.findById(product.getId()).isPresent());
    }
}