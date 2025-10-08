package com.onlineShop.bootcamp.service.order;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.order.OrderPreviewResponse;
import com.onlineShop.bootcamp.dto.order.OrderRequest;
import com.onlineShop.bootcamp.dto.order.OrderResponse;
import com.onlineShop.bootcamp.entity.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {
    OrderResponse createOrder(Long userId, OrderRequest orderRequest);
    List<OrderResponse> getUserOrders(Long userId);
    List<OrderResponse> getAllOrders();
    OrderPreviewResponse previewOrder(OrderRequest orderRequest);
}
