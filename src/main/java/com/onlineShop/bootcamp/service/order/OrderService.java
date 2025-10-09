package com.onlineShop.bootcamp.service.order;

import com.onlineShop.bootcamp.dto.CreateOrderRequest;
import com.onlineShop.bootcamp.dto.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponse placeOrder(String username, CreateOrderRequest request);
    OrderResponse getMyOrder(Long orderId, String username);
    Page<OrderResponse> listMyOrders(Pageable pageable, String username);
}