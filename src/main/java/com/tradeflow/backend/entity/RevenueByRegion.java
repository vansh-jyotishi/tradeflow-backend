package com.tradeflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "revenue_by_region")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RevenueByRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region_name", nullable = false, length = 100)
    private String regionName;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal revenue;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentage;

    @Column(nullable = false)
    private Integer year;

    @Column(length = 20)
    private String color;
}
