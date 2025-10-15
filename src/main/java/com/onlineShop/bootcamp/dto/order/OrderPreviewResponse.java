package com.onlineShop.bootcamp.dto.order;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Summary returned when previewing an order before creation")
public class OrderPreviewResponse {

    @Schema(description = "Total price of all items before adjustments", example = "59.99")
    private double totalPrice;
    @Schema(description = "Estimated shipping cost", example = "4.99")
    private double shippingCost;
    @ArraySchema(schema = @Schema(description = "Preview of the items that will be ordered"))
    private List<OrderItemResponse> items;

}
