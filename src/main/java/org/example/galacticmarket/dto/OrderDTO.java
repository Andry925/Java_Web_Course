package org.example.galacticmarket.dto;
import lombok.Builder;
import lombok.Value;
import org.example.galacticmarket.domain.Product;

import java.time.LocalDateTime;
import java.util.List;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class OrderDTO {
    UUID id;
    List<Product> products;
    String customerName;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;


}