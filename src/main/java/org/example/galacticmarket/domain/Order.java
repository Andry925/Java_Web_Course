package org.example.galacticmarket.domain;



import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

import java.util.UUID;

@Value
@Builder
public class Order {
    UUID id;
    List<Product> products;
    String customerName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}