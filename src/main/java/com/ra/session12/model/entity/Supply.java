package com.ra.session12.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "supplies")
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "provider")
    private String provider;

    @Column(name = "specification")
    private String specification;

    @Column(name = "unit")
    private String unit;

    @Builder.Default
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}