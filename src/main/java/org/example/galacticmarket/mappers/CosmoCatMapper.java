package org.example.galacticmarket.mappers;

import org.example.galacticmarket.dto.CosmocatDTO;
import org.example.galacticmarket.domain.CosmoCat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CosmoCatMapper {

    CosmocatDTO toDTO(CosmoCat cosmocat);

    CosmoCat toEntity(CosmocatDTO cosmocatDTO);

}
