package com.onlineShop.bootcamp.mapper;

import com.onlineShop.bootcamp.dto.order.OrderItemResponse;
import com.onlineShop.bootcamp.entity.OrderItem;


public class OrderItemMapper {

    public static OrderItemResponse toOrderItemResponse(OrderItem orderItem) {

        Double subtotal = orderItem.getPrice().doubleValue() * orderItem.getQuantity();
        return new OrderItemResponse(
                orderItem.getProduct().getId(),
                orderItem.getProduct().getProductName(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                subtotal
        );
    }
}
