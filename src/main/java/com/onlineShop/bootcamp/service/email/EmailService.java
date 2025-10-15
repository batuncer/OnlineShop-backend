package com.onlineShop.bootcamp.service.email;

import com.onlineShop.bootcamp.entity.Order;
import com.onlineShop.bootcamp.entity.User;

public interface EmailService {
    void sendOrderConfirmation(Order order);
    void sendOrderCancellation(Order order);
    void sendRegistrationWelcome(User user);
}
