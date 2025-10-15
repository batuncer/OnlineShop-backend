package com.onlineShop.bootcamp.service.email;

import com.onlineShop.bootcamp.entity.Order;

public interface EmailService {
    void sendOrderConfirmation(Order order);
    void sendOrderCancellation(Order order);
}
