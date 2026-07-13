package com.tonny.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "title", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String title;

    @Column(name = "description", unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String description;

    @Column(name = "reviews", nullable = false)
    @ColumnDefault(value = "0")
    private Double reviews = 0.0;

    @Column(name = "reviews_count", nullable = false)
    @ColumnDefault(value = "0")
    private Integer reviewsCount = 0;

    @Column(name = "price", nullable = false)
    private Double price;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Product(String title, String description, Double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }
}
