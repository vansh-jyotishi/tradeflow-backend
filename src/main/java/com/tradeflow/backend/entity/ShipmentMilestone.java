package com.tradeflow.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipment_milestones")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ShipmentMilestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private Shipment shipment;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 200)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    @Builder.Default
    private MilestoneStatus status = MilestoneStatus.pending;

    @Column(name = "milestone_date")
    private LocalDateTime milestoneDate;

    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;

    public enum MilestoneStatus {
        completed, current, pending
    }
}
