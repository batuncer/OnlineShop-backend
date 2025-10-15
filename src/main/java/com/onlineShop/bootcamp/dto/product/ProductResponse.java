package com.onlineShop.bootcamp.dto.product;

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
public class ProductResponse {
    private Long id;
    private String supplierName;
    private String typeName;
    private Integer amountGrams;
    private Double priceGbp;
    private BrewColor brewColor;
    private Integer caffeineMgPerG;
    private String mainMedicinalUse;
    private Integer recommendedGramsPerCup;
    private Integer stockQuantity;
    private String description;
    private String category;
}
