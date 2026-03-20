package com.tradeflow.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shipments")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_id", nullable = false, unique = true, length = 50)
    private String trackingId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private ShipmentStatus status;

    @Column(name = "transport_mode", nullable = false, length = 20)
    private String transportMode;

    @Column(name = "origin_city", nullable = false, length = 100)
    private String originCity;

    @Column(name = "origin_country", nullable = false, length = 100)
    private String originCountry;

    @Column(name = "dest_city", nullable = false, length = 100)
    private String destCity;

    @Column(name = "dest_country", nullable = false, length = 100)
    private String destCountry;

    @Column(name = "cargo_type", length = 100)
    private String cargoType;

    @Column(name = "weight_tons", precision = 10, scale = 2)
    private BigDecimal weightTons;

    @Column(name = "container_count")
    @Builder.Default
    private Integer containerCount = 0;

    @Column(name = "pallet_count")
    @Builder.Default
    private Integer palletCount = 0;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    private LocalDate eta;

    @Column(name = "delivered_date")
    private LocalDate deliveredDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sortOrder ASC")
    @Builder.Default
    private List<ShipmentMilestone> milestones = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
