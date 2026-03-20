package com.tradeflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipment_statuses")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ShipmentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 20)
    @Builder.Default
    private String color = "#00d4ff";
}
