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
@Schema(description = "Request payload describing an order line item")
public class OrderItemRequest {
    @Schema(description = "Identifier of the product being ordered", example = "101")
    private Long productId;
    @Schema(description = "Quantity of the product to include in the order", example = "2")
    private int quantity;
}
