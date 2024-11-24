package org.example.galacticmarket.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Value
@Builder(toBuilder = true)
public class CategoryDTO {

    private static final String CATEGORY_NAME_EMPTY_MESSAGE = "Category name cannot be empty";
    private static final String CATEGORY_NAME_LENGTH_MESSAGE = "Category name must be between 4 and 30 characters";

    UUID id;

    @NotNull(message = CATEGORY_NAME_EMPTY_MESSAGE)
    @Size(min = 4, max = 30, message = CATEGORY_NAME_LENGTH_MESSAGE)
    String name;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
