package com.onlineShop.bootcamp.service.email;

import com.onlineShop.bootcamp.entity.Order;
import com.onlineShop.bootcamp.entity.OrderItem;
import com.onlineShop.bootcamp.entity.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@onlineshop.local}")
    private String fromEmail;

    @Override
    public void sendOrderConfirmation(Order order) {
        sendTemplate(order, "Order Confirmation",
                buildMessage(order, "Thank you for your order! The details are below:"));
    }

    @Override
    public void sendOrderCancellation(Order order) {
        sendTemplate(order, "Order Cancellation",
                buildMessage(order, "Your order has been cancelled. The details were:"));
    }

    private void sendTemplate(Order order, String subject, String body) {
        User user = order != null ? order.getUser() : null;
        if (user == null || user.getEmail() == null || user.getEmail().isBlank()) {
            logger.warn("Skipping email with subject '{}' because user email is missing. Order id {}", subject,
                    order != null ? order.getId() : null);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(body);

        try {
            mailSender.send(message);
            logger.info("Sent '{}' email to {} for order id {}", subject, user.getEmail(),
                    order != null ? order.getId() : null);
        } catch (MailException ex) {
            logger.error("Failed to send '{}' email to {} for order id {}: {}", subject, user.getEmail(),
                    order != null ? order.getId() : null, ex.getMessage(), ex);
        }
    }

    private String buildMessage(Order order, String intro) {
        StringBuilder builder = new StringBuilder();
        builder.append(intro).append("\n\n");

        if (order == null) {
            builder.append("Order details unavailable.");
            return builder.toString();
        }

        builder.append("Order ID: ").append(order.getId()).append("\n");
        builder.append("Total Price: ").append(order.getTotalPrice()).append("\n");

        if (order.getOrderItemList() == null || order.getOrderItemList().isEmpty()) {
            builder.append("\nNo items were associated with this order.");
        } else {
            builder.append("\nItems:\n");
            String itemLines = order.getOrderItemList().stream()
                    .map(this::formatItemLine)
                    .collect(Collectors.joining("\n"));
            builder.append(itemLines);
        }

        return builder.toString();
    }

    private String formatItemLine(OrderItem item) {
        String productName = item.getProduct() != null ? item.getProduct().getProductName() : "Unknown product";
        return "- " + productName + " x" + item.getQuantity() + " @ " + item.getPrice();
    }
}
