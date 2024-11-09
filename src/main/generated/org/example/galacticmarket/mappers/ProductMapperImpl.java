package org.example.galacticmarket.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.example.galacticmarket.domain.Product;
import org.example.galacticmarket.dto.ProductDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-09T11:03:03+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO toProductDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO.ProductDTOBuilder productDTO = ProductDTO.builder();

        productDTO.id( product.getId() );
        productDTO.name( product.getName() );
        productDTO.price( product.getPrice() );
        productDTO.categoryId( product.getCategoryId() );
        productDTO.description( product.getDescription() );
        productDTO.createdAt( product.getCreatedAt() );
        productDTO.updatedAt( product.getUpdatedAt() );

        return productDTO.build();
    }

    @Override
    public Product toProduct(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.id( productDTO.getId() );
        product.name( productDTO.getName() );
        product.price( productDTO.getPrice() );
        product.categoryId( productDTO.getCategoryId() );
        product.description( productDTO.getDescription() );
        product.createdAt( productDTO.getCreatedAt() );
        product.updatedAt( productDTO.getUpdatedAt() );

        return product.build();
    }

    @Override
    public List<ProductDTO> toProductDTOList(List<Product> productsList) {
        if ( productsList == null ) {
            return null;
        }

        List<ProductDTO> list = new ArrayList<ProductDTO>( productsList.size() );
        for ( Product product : productsList ) {
            list.add( toProductDTO( product ) );
        }

        return list;
    }

    @Override
    public List<Product> toProductEntityList(List<ProductDTO> productsDTOList) {
        if ( productsDTOList == null ) {
            return null;
        }

        List<Product> list = new ArrayList<Product>( productsDTOList.size() );
        for ( ProductDTO productDTO : productsDTOList ) {
            list.add( toProduct( productDTO ) );
        }

        return list;
    }
}
