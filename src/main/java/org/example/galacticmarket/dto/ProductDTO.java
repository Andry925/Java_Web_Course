package org.example.galacticmarket.dto;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;

@Value
@Builder(toBuilder = true)
public class ProductDTO {

    private static final String PRODUCT_EMPTY_MESSAGE = "Product cannot be empty";
    private static final String PRODUCT_LENGTH_MESSAGE = "Product name must be between 4 and 40 characters";
    private static final String PRICE_REQUIRED_MESSAGE = "Price must be set";
    private static final String PRICE_MINIMUM_MESSAGE = "Price cannot be less than or equal to zero";
    private static final String CATEGORY_ID_EMPTY_MESSAGE = "Category ID cannot be empty";

    UUID id;

    @NotNull(message = "Product cannot be empty")
    @Size(min = 4, max = 40, message = "Product name must be between 4 and 40 characters")
    String name;

    @NotNull(message = "Price cannot be less than or equal to zero")
    @DecimalMin(value = "0.001", message = PRICE_MINIMUM_MESSAGE)
    float price;

    @NotNull(message = "Category ID cannot be empty")
    int categoryId;

    String description;
    String galaxyOrigin;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
