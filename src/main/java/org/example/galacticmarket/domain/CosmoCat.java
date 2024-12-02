package org.example.galacticmarket.domain;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder

public class CosmoCat {
    UUID id;
    String username;
    String email;
}
