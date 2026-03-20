package com.tradeflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "platform_stats")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PlatformStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_key", nullable = false, unique = true, length = 50)
    private String statKey;

    @Column(name = "stat_value", nullable = false, length = 50)
    private String statValue;

    @Column(name = "display_label", nullable = false, length = 100)
    private String displayLabel;

    @Column(name = "icon_class", length = 100)
    private String iconClass;

    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        updatedAt = LocalDateTime.now();
    }
}
