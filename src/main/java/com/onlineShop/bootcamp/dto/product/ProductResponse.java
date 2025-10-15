package com.onlineShop.bootcamp.dto.product;

import com.onlineShop.bootcamp.entity.enums.BrewColor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Product information returned to clients")
public class ProductResponse {
    @Schema(description = "Unique product identifier", example = "501")
    private Long id;
    @Schema(description = "Identifier of the associated supplier", example = "7")
    private Long supplierId;
    @Schema(description = "Name of the tea or coffee type", example = "Chamomile Blossom")
    private String typeName;
    @Schema(description = "Net weight of the product in grams", example = "250")
    private Integer amountGrams;
    @Schema(description = "Price in GBP", example = "14.49")
    private Double priceGbp;
    @Schema(description = "Colour category of the brew")
    private BrewColor brewColor;
    @Schema(description = "Caffeine amount per gram", example = "3")
    private Integer caffeineMgPerG;
    @Schema(description = "Primary medicinal or wellness use", example = "Supports relaxation and sleep")
    private String mainMedicinalUse;
    @Schema(description = "Recommended grams of product per cup", example = "5")
    private Integer recommendedGramsPerCup;
    @Schema(description = "Quantity available in stock", example = "120")
    private Integer stockQuantity;
    @Schema(description = "Detailed description displayed to customers", example = "Organic chamomile blossoms with delicate flavour.")
    private String description;
    @Schema(description = "Product category", example = "Herbal Tea")
    private String category;
}
