package org.example.galacticmarket.dto;




import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class CategoryDTO {
    UUID id;
    String name;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}