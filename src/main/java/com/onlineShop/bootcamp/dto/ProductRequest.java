package com.onlineShop.bootcamp.dto;

import com.onlineShop.bootcamp.entity.enums.BrewColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private Long supplierId;
    private String typeName;
    private Integer amountGrams;
    private BigDecimal priceGbp;
    private BrewColor brewColor;
    private Integer caffeineMgPerG;
    private String mainMedicinalUse;
    private Integer recommendedGramsPerCup;
    private Integer stockQuantity;
    private String description;
    private String category;
}