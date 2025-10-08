package com.onlineShop.bootcamp.mapper;

import com.onlineShop.bootcamp.dto.order.OrderItemResponse;
import com.onlineShop.bootcamp.dto.order.OrderResponse;
import com.onlineShop.bootcamp.entity.Order;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public static OrderResponse toOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getUser().getUsername(),
                order.getOrderDate(),
                order.getTotalPrice().doubleValue(),
                order.getShippingCost().doubleValue(),
                order.getOrderItemList().stream()
                        .map(OrderItemMapper::toOrderItemResponse)
                        .collect(Collectors.toList())
        );
    }
}
