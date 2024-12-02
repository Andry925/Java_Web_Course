package org.example.galacticmarket.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class CosmocatDTO {

    @NotBlank(message = "Username must be provided")
    @Size(max = 30, message = "Name cannot be more than 30 characters")
    String username;

    @Email(message = "Email must be provided")
    String email;

    public CosmocatDTO(String username, String email) {
        this.username = username;
        this.email = email;
    }
}