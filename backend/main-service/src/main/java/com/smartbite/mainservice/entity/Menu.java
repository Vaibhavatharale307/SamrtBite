package com.smartbite.mainservice.entity;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    @ManyToOne
    @JoinColumn(name = "canteen_id", nullable = false)
    private Canteen canteen;

    @Column(nullable = false)
    private String foodName;

    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    private String category;

    private Boolean available;

    private String imageUrl;
}
