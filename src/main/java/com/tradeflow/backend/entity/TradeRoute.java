package com.tradeflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "trade_routes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TradeRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "origin_city", nullable = false, length = 100)
    private String originCity;

    @Column(name = "origin_lat", precision = 10, scale = 7)
    private BigDecimal originLat;

    @Column(name = "origin_lng", precision = 10, scale = 7)
    private BigDecimal originLng;

    @Column(name = "dest_city", nullable = false, length = 100)
    private String destCity;

    @Column(name = "dest_lat", precision = 10, scale = 7)
    private BigDecimal destLat;

    @Column(name = "dest_lng", precision = 10, scale = 7)
    private BigDecimal destLng;

    @Column(name = "route_color", length = 20)
    @Builder.Default
    private String routeColor = "#00d4ff";

    @Builder.Default
    private Integer volume = 0;

    @Column(name = "avg_transit_days", precision = 5, scale = 1)
    private BigDecimal avgTransitDays;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;
}
