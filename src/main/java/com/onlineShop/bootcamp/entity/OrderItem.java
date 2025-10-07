package com.onlineShop.bootcamp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "unit_price_gbp", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "amount_grams_per_unit")
    private Integer amountGramsPerUnit;

    @Column(name = "line_total_gbp", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;
}
