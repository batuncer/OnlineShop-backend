package com.onlineShop.bootcamp.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String shippingAddress;
    private List<Line> lines;

    @Data
    public static class Line {
        private Long productId;
        private int quantity;
    }
}
