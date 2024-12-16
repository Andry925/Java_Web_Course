package org.example.galacticmarket.service;


import org.example.galacticmarket.dto.ProductDTO;
import org.example.galacticmarket.mappers.ProductMapper;
import org.example.galacticmarket.repository.ProductRepository;
import org.example.galacticmarket.repository.entity.ProductEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productMapper.toProductDTOList((List<ProductEntity>) productRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toProductDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }



    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        ProductEntity product = productMapper.toProductEntity(productDTO);
        product.setId(UUID.randomUUID());
        ProductEntity savedProduct = productRepository.save(product);
        return productMapper.toProductDTO(savedProduct);
    }

    @Transactional
    public ProductDTO updateProduct(UUID id, ProductDTO productDTO) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setGalaxyOrigin(productDTO.getGalaxyOrigin());
        return productMapper.toProductDTO(productRepository.save(product));
    }

    @Transactional
    public boolean deleteProduct(UUID id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}