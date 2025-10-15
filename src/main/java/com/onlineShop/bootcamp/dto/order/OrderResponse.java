package com.onlineShop.bootcamp.dto.order;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Order information returned after creation or retrieval")
public class OrderResponse {

    @Schema(description = "Order identifier", example = "550")
    private Long id;
    @Schema(description = "Identifier of the user who placed the order", example = "42")
    private Long userId;
    @Schema(description = "Date and time when the order was created", example = "2024-03-21T14:32:00")
    private LocalDateTime orderDate;
    @Schema(description = "Total order amount including shipping", example = "64.99")
    private Double totalAmount;
    @Schema(description = "Shipping cost applied to the order", example = "4.99")
    private Double shippingCost;
    @ArraySchema(schema = @Schema(description = "Line items included in the order"))
    private List<OrderItemResponse> orderItems;
}
