package com.tradeflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_analytics", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"year", "month"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonthlyAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @Column(name = "total_shipments")
    @Builder.Default
    private Integer totalShipments = 0;

    @Column(precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal revenue = BigDecimal.ZERO;

    @Column(name = "avg_transit_days", precision = 5, scale = 1)
    @Builder.Default
    private BigDecimal avgTransitDays = BigDecimal.ZERO;

    @Column(name = "delivery_rate", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal deliveryRate = BigDecimal.ZERO;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
