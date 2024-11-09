package org.example.galacticmarket.controllers;

import org.example.galacticmarket.dto.ProductDTO;
import org.example.galacticmarket.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDTO mockProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockProduct = ProductDTO.builder()
                .id(UUID.randomUUID())
                .name("cosmo milk")
                .price(20)
                .categoryId(1)
                .description("high quality milk")
                .galaxyOrigin("Milky way")
                .build();
    }

    @Test
    void getAllProducts() {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(mockProduct));

        ResponseEntity<List<ProductDTO>> response = productController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById() {
        UUID productId = mockProduct.getId();
        when(productService.getProductById(productId)).thenReturn(mockProduct);

        ResponseEntity<ProductDTO> response = productController.getProductById(productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockProduct, response.getBody());
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void getProductByNonExistingId() {
        UUID productId = UUID.randomUUID();
        when(productService.getProductById(productId)).thenReturn(null);

        ResponseEntity<ProductDTO> response = productController.getProductById(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService, times(1)).getProductById(productId);
    }

    @Test
    void createProduct() {
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(mockProduct);

        ResponseEntity<ProductDTO> response = productController.createProduct(mockProduct);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockProduct, response.getBody());
        verify(productService, times(1)).createProduct(any(ProductDTO.class));
    }

    @Test
    void updateProduct() {
        UUID productId = mockProduct.getId();
        when(productService.updateProduct(eq(productId), any(ProductDTO.class))).thenReturn(mockProduct);

        ResponseEntity<ProductDTO> response = productController.updateProduct(productId, mockProduct);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockProduct, response.getBody());
        verify(productService, times(1)).updateProduct(eq(productId), any(ProductDTO.class));
    }

    @Test
    void update_NonExistingProduct() {
        UUID productId = UUID.randomUUID();
        when(productService.updateProduct(eq(productId), any(ProductDTO.class))).thenReturn(null);

        ResponseEntity<ProductDTO> response = productController.updateProduct(productId, mockProduct);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService, times(1)).updateProduct(eq(productId), any(ProductDTO.class));
    }

    @Test
    void deleteProduct() {
        UUID productId = mockProduct.getId();
        when(productService.deleteProduct(productId)).thenReturn(true);

        ResponseEntity<Void> response = productController.deleteProduct(productId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    void deleteNonExistingProduct() {
        UUID productId = UUID.randomUUID();
        when(productService.deleteProduct(productId)).thenReturn(false);

        ResponseEntity<Void> response = productController.deleteProduct(productId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(productId);
    }
}
