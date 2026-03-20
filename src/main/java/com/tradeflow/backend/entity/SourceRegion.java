package com.tradeflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "source_regions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SourceRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 10)
    private String code;
}
