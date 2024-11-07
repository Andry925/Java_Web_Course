package org.example.galacticmarket.service;

import org.example.galacticmarket.domain.Product;
import org.example.galacticmarket.dto.ProductDTO;
import org.example.galacticmarket.mappers.ProductMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ProductService {

    private final Map<UUID, Product> productDatabase = new HashMap<>();
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
        initializeMockData();
    }

    private void initializeMockData() {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("cosmo milk")
                .price(20)
                .categoryId(1)
                .description("high quality milk")
                .galaxy_origin("Milky way")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productDatabase.put(product.getId(), product);
    }

    public List<ProductDTO> getAllProducts() {
        return productMapper.toProductDTOList(new ArrayList<>(productDatabase.values()));
    }

    public ProductDTO getProductById(UUID id) {
        Product product = productDatabase.get(id);
        return product != null ? productMapper.toProductDTO(product) : null;
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .categoryId(productDTO.getCategoryId())
                .description(productDTO.getDescription())
                .galaxy_origin(productDTO.getGalaxyOrigin())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        productDatabase.put(product.getId(), product);
        return productMapper.toProductDTO(product);
    }

    public ProductDTO updateProduct(UUID id, ProductDTO productDTO) {
        Product existingProduct = productDatabase.get(id);
        if (existingProduct != null) {
            Product updatedProduct = Product.builder()
                    .id(id)
                    .name(productDTO.getName())
                    .price(productDTO.getPrice())
                    .categoryId(productDTO.getCategoryId())
                    .description(productDTO.getDescription())
                    .galaxy_origin(productDTO.getGalaxyOrigin())
                    .createdAt(existingProduct.getCreatedAt())
                    .updatedAt(LocalDateTime.now())
                    .build();

            productDatabase.put(id, updatedProduct);
            return productMapper.toProductDTO(updatedProduct);
        }
        return null;
    }

    public boolean deleteProduct(UUID id) {
        return productDatabase.remove(id) != null;
    }
}
