package com.productivityhub.finance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "categories", schema = "finance")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 16)
    private String color;

    @Column(length = 64)
    private String icon;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Transaction.TransactionType type;
}
