package org.example.galacticmarket.mappers;


import org.example.galacticmarket.dto.CategoryDTO;
import org.example.galacticmarket.domain.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(Category category);

    Category toEntity(CategoryDTO categoryDTO);
}