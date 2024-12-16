package org.example.galacticmarket.service;

import org.example.galacticmarket.dto.ProductDTO;
import org.example.galacticmarket.mappers.ProductMapper;
import org.example.galacticmarket.mappers.ProductMapperImpl;
import org.example.galacticmarket.repository.ProductRepository;
import org.example.galacticmarket.repository.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productMapper = new ProductMapperImpl();
        productService = new ProductService(productRepository, productMapper);
    }

    @Test
    void testGetAllProducts() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(UUID.randomUUID());
        productEntity.setName("cosmo milk");
        productEntity.setPrice(10.5f);

        when(productRepository.findAll()).thenReturn(Collections.singletonList(productEntity));

        List<ProductDTO> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("cosmo milk", products.get(0).getName());
        verify(productRepository, times(1)).findAll(); // Verify the method was called once
    }

    @Test
    void testGetProductById_existingProduct() {
        UUID existingProductId = UUID.randomUUID();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(existingProductId);
        productEntity.setName("cosmo milk");

        when(productRepository.findById(existingProductId)).thenReturn(Optional.of(productEntity));

        ProductDTO productDTO = productService.getProductById(existingProductId);

        assertNotNull(productDTO);
        assertEquals("cosmo milk", productDTO.getName());
        verify(productRepository, times(1)).findById(existingProductId);
    }

    @Test
    void testGetProductById_nonExistentProduct() {
        UUID nonExistentId = UUID.randomUUID();

        when(productRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getProductById(nonExistentId));
        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void testCreateProduct() {
        ProductDTO newProduct = ProductDTO.builder()
                .name("star juice")
                .price(15.5f)
                .category_id(UUID.randomUUID())
                .description("Fresh star juice")
                .galaxyOrigin("Andromeda")
                .build();

        ProductEntity productEntity = productMapper.toProductEntity(newProduct);
        productEntity.setId(UUID.randomUUID());

        when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(productEntity);

        ProductDTO createdProduct = productService.createProduct(newProduct);

        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getId());
        assertEquals(newProduct.getName(), createdProduct.getName());
        verify(productRepository, times(1)).save(Mockito.any(ProductEntity.class));
    }

    @Test
    void testUpdateProduct_existingProduct() {
        UUID existingProductId = UUID.randomUUID();
        ProductEntity existingEntity = new ProductEntity();
        existingEntity.setId(existingProductId);
        existingEntity.setName("cosmo milk");

        ProductDTO updatedProduct = ProductDTO.builder()
                .name("cosmic yogurt")
                .price(25.0f)
                .category_id(UUID.randomUUID())
                .description("Delicious cosmic yogurt")
                .galaxyOrigin("Milky way")
                .build();

        when(productRepository.findById(existingProductId)).thenReturn(Optional.of(existingEntity));
        when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(existingEntity);

        ProductDTO result = productService.updateProduct(existingProductId, updatedProduct);

        assertNotNull(result);
        assertEquals(updatedProduct.getName(), result.getName());
        verify(productRepository, times(1)).findById(existingProductId);
        verify(productRepository, times(1)).save(Mockito.any(ProductEntity.class));
    }

    @Test
    void testDeleteProduct_existingProduct() {
        UUID existingProductId = UUID.randomUUID();

        when(productRepository.existsById(existingProductId)).thenReturn(true);

        boolean result = productService.deleteProduct(existingProductId);

        assertTrue(result);
        verify(productRepository, times(1)).existsById(existingProductId);
        verify(productRepository, times(1)).deleteById(existingProductId);
    }

    @Test
    void testDeleteProduct_nonExistentProduct() {
        UUID nonExistentId = UUID.randomUUID();

        when(productRepository.existsById(nonExistentId)).thenReturn(false);

        boolean result = productService.deleteProduct(nonExistentId);

        assertFalse(result);
        verify(productRepository, times(1)).existsById(nonExistentId);
        verify(productRepository, never()).deleteById(nonExistentId);
    }
}