package com.onlineShop.bootcamp.entity;


import com.onlineShop.bootcamp.entity.enums.BrewColor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "amount_grams")
    private Integer amountGrams;

    @Column(name = "price_gbp")
    private Double priceGbp;

    private String category;

    @Column(name = "caffeine_mg_per_g")
    private Integer caffeineMgPerG;

    @Column(name = "brew_color")
    @Enumerated(EnumType.STRING)
    private BrewColor brewColor;

    @Column(name = "main_medicinal_use")
    private String mainMedicinalUse;

    @Column(name = "recommended_grams_per_cup")
    private Integer recommendedGramsPerCup;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
