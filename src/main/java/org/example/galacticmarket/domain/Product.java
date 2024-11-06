package org.example.galacticmarket.domain;


import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class Product {
    UUID id;
    String name;
    float price;
    int categoryId;
    String description;
    String galaxy_origin;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}