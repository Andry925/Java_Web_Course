package org.example.galacticmarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galacticmarket.dto.OrderDTO;
import org.example.galacticmarket.repository.CategoryRepository;
import org.example.galacticmarket.repository.OrderRepository;
import org.example.galacticmarket.repository.ProductRepository;
import org.example.galacticmarket.repository.entity.CategoryEntity;
import org.example.galacticmarket.repository.entity.OrderEntity;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.8-alpine3.18")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @BeforeAll
    static void startContainer() {
        postgres.start();
    }

    @AfterAll
    static void stopContainer() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity productEntity;
    private CategoryEntity categoryEntity;

    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        categoryEntity = categoryRepository.save(CategoryEntity.builder()
                .id(UUID.randomUUID())
                .name("Galactic Goods")
                .build());

        productEntity = productRepository.save(ProductEntity.builder()
                .id(UUID.randomUUID())
                .name("Cosmic Product")
                .price(15.99f)
                .description("A product from the stars")
                .category(categoryEntity)
                .build());
    }

    @Test
    void testCreateOrderValid() throws Exception {
        OrderDTO orderDTO = OrderDTO.builder()
                .customerName("Interstellar Buyer")
                .products(List.of(productEntity.getId()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("Interstellar Buyer"))
                .andExpect(jsonPath("$.products[0]").value(productEntity.getId().toString()));
    }

    @Test
    void testGetAllOrders() throws Exception {
        OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .id(UUID.randomUUID())
                .customerName("Galactic Trader")
                .products(Collections.singletonList(productEntity))
                .build());

        mockMvc.perform(get("/api/v1/orders")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Galactic Trader"))
                .andExpect(jsonPath("$[0].products[0]").value(productEntity.getId().toString()));
    }

    @Test
    void testGetOrderById() throws Exception {
        OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .id(UUID.randomUUID())
                .customerName("Space Explorer")
                .products(Collections.singletonList(productEntity))
                .build());

        mockMvc.perform(get("/api/v1/orders/{id}", orderEntity.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Space Explorer"))
                .andExpect(jsonPath("$.products[0]").value(productEntity.getId().toString()));
    }

    @Test
    void testUpdateOrder() throws Exception {
        OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .id(UUID.randomUUID())
                .customerName("Old Name")
                .products(Collections.singletonList(productEntity))
                .build());

        OrderDTO updatedOrderDTO = OrderDTO.builder()
                .customerName("Updated Buyer")
                .products(List.of(productEntity.getId()))
                .updatedAt(LocalDateTime.now())
                .build();

        mockMvc.perform(put("/api/v1/orders/{id}", orderEntity.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedOrderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Updated Buyer"))
                .andExpect(jsonPath("$.products[0]").value(productEntity.getId().toString()));
    }

    @Test
    void testDeleteOrder() throws Exception {
        OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .id(UUID.randomUUID())
                .customerName("To Be Deleted")
                .products(Collections.singletonList(productEntity))
                .build());

        mockMvc.perform(delete("/api/v1/orders/{id}", orderEntity.getId()))
                .andExpect(status().isNoContent());

        assertFalse(orderRepository.findById(orderEntity.getId()).isPresent());
    }
}