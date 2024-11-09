package org.example.galacticmarket.mappers;

import javax.annotation.processing.Generated;
import org.example.galacticmarket.domain.Category;
import org.example.galacticmarket.dto.CategoryDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-08T14:54:55+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public CategoryDTO toDTO(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDTO.CategoryDTOBuilder categoryDTO = CategoryDTO.builder();

        categoryDTO.id( category.getId() );
        categoryDTO.name( category.getName() );
        categoryDTO.createdAt( category.getCreatedAt() );
        categoryDTO.updatedAt( category.getUpdatedAt() );

        return categoryDTO.build();
    }

    @Override
    public Category toEntity(CategoryDTO categoryDTO) {
        if ( categoryDTO == null ) {
            return null;
        }

        Category.CategoryBuilder category = Category.builder();

        category.id( categoryDTO.getId() );
        category.name( categoryDTO.getName() );
        category.createdAt( categoryDTO.getCreatedAt() );
        category.updatedAt( categoryDTO.getUpdatedAt() );

        return category.build();
    }
}
