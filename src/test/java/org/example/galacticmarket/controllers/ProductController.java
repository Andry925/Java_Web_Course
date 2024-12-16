package org.example.galacticmarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.galacticmarket.dto.ProductDTO;
import org.example.galacticmarket.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ObjectMapper objectMapper;
    private ProductDTO mockProduct;

    private UUID mockCategoryId;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockCategoryId = UUID.randomUUID();

        mockProduct = ProductDTO.builder()
                .id(UUID.randomUUID())
                .name("cosmo milk")
                .price(20)
                .category_id(mockCategoryId)
                .description("high quality milk")
                .galaxyOrigin("Milky way")
                .build();
    }

    @Test
    void getAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(mockProduct));

        mockMvc.perform(get("http://127.0.0.1:8080/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("cosmo milk"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById() throws Exception {
        UUID productId = mockProduct.getId();
        when(productService.getProductById(productId)).thenReturn(mockProduct);

        mockMvc.perform(get("http://127.0.0.1:8080/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("cosmo milk"));

        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void getProductByNonExistingId() throws Exception {
        UUID productId = UUID.randomUUID();
        when(productService.getProductById(productId)).thenReturn(null);

        mockMvc.perform(get("http://127.0.0.1:8080/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void createProduct() throws Exception {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(mockProduct);

        mockMvc.perform(post("http://127.0.0.1:8080/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("cosmo milk"));

        verify(productService, times(1)).createProduct(any(ProductDTO.class));
    }

    @Test
    void updateProduct() throws Exception {
        UUID productId = mockProduct.getId();
        when(productService.updateProduct(eq(productId), any(ProductDTO.class))).thenReturn(mockProduct);

        mockMvc.perform(put("http://127.0.0.1:8080/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("cosmo milk"));

        verify(productService, times(1)).updateProduct(eq(productId), any(ProductDTO.class));
    }

    @Test
    void updateNonExistingProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        when(productService.updateProduct(eq(productId), any(ProductDTO.class))).thenReturn(null);

        mockMvc.perform(put("http://127.0.0.1:8080/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProduct)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).updateProduct(eq(productId), any(ProductDTO.class));
    }

    @Test
    void deleteProduct() throws Exception {
        UUID productId = mockProduct.getId();
        when(productService.deleteProduct(productId)).thenReturn(true);

        mockMvc.perform(delete("http://127.0.0.1:8080/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void deleteNonExistingProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        when(productService.deleteProduct(productId)).thenReturn(false);

        mockMvc.perform(delete("http://127.0.0.1:8080/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).deleteProduct(productId);
    }
}
