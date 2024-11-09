package org.example.galacticmarket.service;
import org.example.galacticmarket.dto.ProductDTO;
import org.example.galacticmarket.mappers.ProductMapper;
import org.example.galacticmarket.mappers.ProductMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class ProductServiceTest {

    private ProductService productService;
    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapperImpl();
        productService = new ProductService(productMapper);
    }

    @Test
    void testGetAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("cosmo milk", products.get(0).getName());
    }

    @Test
    void testGetProductById_existingProduct() {
        UUID existingProductId = productService.getAllProducts().get(0).getId();

        ProductDTO productDTO = productService.getProductById(existingProductId);

        assertNotNull(productDTO);
        assertEquals("cosmo milk", productDTO.getName());
    }

    @Test
    void testGetProductById_nonExistentProduct() {
        UUID nonExistentId = UUID.randomUUID();

        ProductDTO productDTO = productService.getProductById(nonExistentId);

        assertNull(productDTO);
    }

    @Test
    void testCreateProduct() {
        ProductDTO newProduct = ProductDTO.builder()
                .name("star juice")
                .price(15.5f)
                .categoryId(2)
                .description("Fresh star juice")
                .galaxyOrigin("Andromeda")
                .build();

        ProductDTO createdProduct = productService.createProduct(newProduct);

        assertNotNull(createdProduct);
        assertNotNull(createdProduct.getId());
        assertEquals(newProduct.getName(), createdProduct.getName());
    }

    @Test
    void testUpdateProduct_existingProduct() {
        UUID existingProductId = productService.getAllProducts().get(0).getId();

        ProductDTO updatedProduct = ProductDTO.builder()
                .name("cosmic yogurt")
                .price(25.0f)
                .categoryId(3)
                .description("Delicious cosmic yogurt")
                .galaxyOrigin("Milky way")
                .build();

        ProductDTO result = productService.updateProduct(existingProductId, updatedProduct);

        assertNotNull(result);
        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        assertEquals(updatedProduct.getDescription(), result.getDescription());
    }

    @Test
    void testUpdateProduct_nonExistentProduct() {
        UUID nonExistentId = UUID.randomUUID();

        ProductDTO updatedProduct = ProductDTO.builder()
                .name("galactic ice cream")
                .price(30.0f)
                .categoryId(4)
                .description("Frozen from the depths of space")
                .galaxyOrigin("Andromeda")
                .build();

        ProductDTO result = productService.updateProduct(nonExistentId, updatedProduct);

        assertNull(result);
    }

    @Test
    void testDeleteProduct_existingProduct() {
        UUID existingProductId = productService.getAllProducts().get(0).getId();

        boolean result = productService.deleteProduct(existingProductId);

        assertTrue(result);
        assertNull(productService.getProductById(existingProductId));
    }

    @Test
    void testDeleteProduct_nonExistentProduct() {
        UUID nonExistentId = UUID.randomUUID();

        boolean result = productService.deleteProduct(nonExistentId);

        assertFalse(result);
    }
}
