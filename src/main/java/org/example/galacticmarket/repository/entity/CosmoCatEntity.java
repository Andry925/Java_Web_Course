package org.example.galacticmarket.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "cosmocat")
public class CosmoCatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cosmocat_id_seq")
    @SequenceGenerator(
            name = "cosmocat_id_seq",
            sequenceName = "cosmocat_id_seq",
            allocationSize = 50
    )
    Long id;

    @Column(nullable = false, length = 30)
    String username;

    @Column(nullable = false, unique = true)
    String email;
}
