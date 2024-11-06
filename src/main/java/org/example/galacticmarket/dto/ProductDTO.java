package org.example.galacticmarket.dto;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class ProductDTO {
    UUID id;
    String name;
    float price;
    int categoryId;
    String description;
    String galaxy_origin;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}