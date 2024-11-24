package org.example.galacticmarket.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class Category {
    UUID id;
    String name;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}