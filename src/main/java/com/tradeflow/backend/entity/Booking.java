package com.tradeflow.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_number", nullable = false, unique = true, length = 50)
    private String bookingNumber;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    @Builder.Default
    private BookingStatus status = BookingStatus.PENDING;

    // Cargo details
    @Column(name = "cargo_description", nullable = false, length = 500)
    private String cargoDescription;

    @Column(name = "cargo_type", length = 100)
    private String cargoType;

    @Column(name = "weight_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal weightKg;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 1;

    // Route
    @Column(name = "origin_city", nullable = false, length = 100)
    private String originCity;

    @Column(name = "origin_country", nullable = false, length = 100)
    private String originCountry;

    @Column(name = "dest_city", nullable = false, length = 100)
    private String destCity;

    @Column(name = "dest_country", nullable = false, length = 100)
    private String destCountry;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_mode", nullable = false, length = 10)
    private TransportMode transportMode;

    // Pricing
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(length = 3)
    @Builder.Default
    private String currency = "USD";

    @Builder.Default
    private Boolean paid = false;

    // Dates
    @Column(name = "pickup_date")
    private LocalDate pickupDate;

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    @Column(name = "actual_delivery")
    private LocalDate actualDelivery;

    @Column(columnDefinition = "TEXT")
    private String notes;

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

    public enum BookingStatus {
        PENDING, CONFIRMED, IN_TRANSIT, DELIVERED, CANCELLED
    }

    public enum TransportMode {
        Maritime, Air, Land, Rail
    }
}
