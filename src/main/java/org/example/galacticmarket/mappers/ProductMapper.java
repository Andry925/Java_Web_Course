package org.example.galacticmarket.mappers;


import org.example.galacticmarket.domain.Product;
import org.example.galacticmarket.dto.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO toProductDTO(Product product);

    Product toProduct(ProductDTO productDTO);

    List<ProductDTO> toProductDTOList(List<Product> productsList);

    List<Product> toProductEntityList(List<ProductDTO> productsDTOList);
}
