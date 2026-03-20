package com.tradeflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carrier_types")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CarrierType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String slug;
}
