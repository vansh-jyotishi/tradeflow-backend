package com.tradeflow.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inquiry_types")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class InquiryType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;
}
