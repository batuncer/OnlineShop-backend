package com.onlineShop.bootcamp.service.order;

import com.onlineShop.bootcamp.dto.order.OrderPreviewResponse;
import com.onlineShop.bootcamp.dto.order.OrderRequest;
import com.onlineShop.bootcamp.dto.order.OrderResponse;
import java.util.List;


public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    List<OrderResponse> getUserOrders();
    List<OrderResponse> getAllOrders();
    OrderPreviewResponse previewOrder(OrderRequest orderRequest);
}
