package com.tradeflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trusted_partners")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TrustedPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "logo_url", nullable = false, length = 500)
    private String logoUrl;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}
