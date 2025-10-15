package com.onlineShop.bootcamp.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Line item returned as part of an order response")
public class OrderItemResponse {

    @Schema(description = "Identifier of the product", example = "101")
    private Long productId;
    @Schema(description = "Display name of the product", example = "Darjeeling First Flush Tea")
    private String productName;
    @Schema(description = "Quantity of the product ordered", example = "2")
    private Integer quantity;
    @Schema(description = "Price per unit in GBP", example = "12.99")
    private Double perItemPrice;
    @Schema(description = "Amount contributed by the item to the order total", example = "25.98")
    private Double subTotal;

}
