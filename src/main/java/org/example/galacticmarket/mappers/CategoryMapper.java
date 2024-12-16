package org.example.galacticmarket.mappers;


import org.example.galacticmarket.dto.CategoryDTO;
import org.example.galacticmarket.domain.Category;
import org.example.galacticmarket.repository.entity.CategoryEntity;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDTO(CategoryEntity category);

    CategoryEntity toEntity(CategoryDTO categoryDTO);

    List<CategoryDTO> toDTOList(List<CategoryEntity> categories);
}