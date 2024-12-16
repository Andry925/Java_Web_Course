package org.example.galacticmarket.mappers;


import org.example.galacticmarket.dto.ProductDTO;
import org.example.galacticmarket.repository.entity.CategoryEntity;
import org.example.galacticmarket.repository.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "category_id")
    ProductDTO toProductDTO(ProductEntity productEntity);

    @Mapping(source = "category_id", target = "category", qualifiedByName = "categoryFromId")
    ProductEntity toProductEntity(ProductDTO productDTO);

    List<ProductDTO> toProductDTOList(List<ProductEntity> productEntities);

    @Named("categoryFromId")
    default CategoryEntity categoryFromId(UUID categoryId) {
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryId);
        return category;
    }
}
