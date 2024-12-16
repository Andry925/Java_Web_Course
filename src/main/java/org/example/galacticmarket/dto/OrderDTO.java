package org.example.galacticmarket.dto;
import jakarta.validation.constraints.NotNull;
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
    String customerName;
    @NotNull(message = "Products cannot be null")
    List<UUID> products;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}