package com.tradeflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "commodity_performance")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CommodityPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal revenue;

    @Column(name = "revenue_share", nullable = false, precision = 5, scale = 2)
    private BigDecimal revenueShare;

    @Column(name = "growth_percent", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal growthPercent = BigDecimal.ZERO;

    @Column(nullable = false)
    private Integer year;
}
