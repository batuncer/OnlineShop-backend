package com.onlineShop.bootcamp.dto.order;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Payload required to place or preview an order")
public class OrderRequest {

    @ArraySchema(schema = @Schema(description = "Collection of items included in the order"))
    private List<OrderItemRequest> orderItems;
}
