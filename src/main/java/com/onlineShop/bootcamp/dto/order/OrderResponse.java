package com.onlineShop.bootcamp.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private String username;
    private LocalDateTime orderDate;
    private Double totalAmount;
    private Double shippingCost;
    private List<OrderItemResponse> orderItems;

}
