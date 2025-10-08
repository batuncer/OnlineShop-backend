package com.onlineShop.bootcamp.dto.order;

import lombok.Data;

@Data
public class OrderItemRequest {

    private Long productid;
    private int quantity;
}
