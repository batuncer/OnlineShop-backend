package com.onlineShop.bootcamp.service.order;

import com.onlineShop.bootcamp.dto.CreateOrderRequest;
import com.onlineShop.bootcamp.dto.CreateOrderRequest.Line;
import com.onlineShop.bootcamp.dto.OrderResponse;
import com.onlineShop.bootcamp.entity.Order;
import com.onlineShop.bootcamp.entity.OrderItem;
import com.onlineShop.bootcamp.entity.Product;
import com.onlineShop.bootcamp.entity.User;
import com.onlineShop.bootcamp.repository.OrderRepository;
import com.onlineShop.bootcamp.repository.ProductRepository;
import com.onlineShop.bootcamp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderResponse placeOrder(String username, CreateOrderRequest request) {
        if (request == null || request.getLines() == null || request.getLines().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one line item");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        int attempts = 0;
        final int maxAttempts = 3;
        while (true) {
            try {
                return doPlaceOrder(user, request);
            } catch (OptimisticLockingFailureException ex) {
                if (++attempts >= maxAttempts) throw ex;
            }
        }
    }

    private OrderResponse doPlaceOrder(User user, CreateOrderRequest request) {
        BigDecimal subtotal = BigDecimal.ZERO;
        int totalWeightGrams = 0;
        List<OrderItem> items = new ArrayList<>();

        for (Line line : request.getLines()) {
            Product p = productRepository.findById(line.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + line.getProductId()));

            int qty = line.getQuantity();
            if (qty <= 0) throw new IllegalArgumentException("Quantity must be > 0 for product " + p.getId());

            int currentStock = p.getStockQuantity() == null ? 0 : p.getStockQuantity();
            if (currentStock < qty) {
                throw new IllegalStateException("Insufficient stock for product " + p.getId());
            }

            p.setStockQuantity(currentStock - qty);

            BigDecimal unit = p.getPriceGbp().setScale(2, RoundingMode.HALF_UP);
            BigDecimal lineTotal = unit.multiply(BigDecimal.valueOf(qty)).setScale(2, RoundingMode.HALF_UP);

            subtotal = subtotal.add(lineTotal);
            totalWeightGrams += (p.getAmountGrams() == null ? 0 : p.getAmountGrams()) * qty;

            items.add(OrderItem.builder()
                    .productId(p.getId())
                    .name(p.getProductName())
                    .unitPrice(unit)
                    .quantity(qty)
                    .amountGramsPerUnit(p.getAmountGrams())
                    .lineTotal(lineTotal)
                    .build());
        }

        BigDecimal shipping = calculateShipping(subtotal, totalWeightGrams);
        BigDecimal total = subtotal.add(shipping).setScale(2, RoundingMode.HALF_UP);

        Order order = Order.builder()
                .user(user)
                .items(items)
                .subtotal(subtotal.setScale(2, RoundingMode.HALF_UP))
                .shipping(shipping)
                .total(total)
                .placedAt(OffsetDateTime.now())
                .shippingAddress(request.getShippingAddress())
                .build();

        items.forEach(i -> i.setOrder(order));

        Order saved = orderRepository.save(order);
        return OrderResponse.from(saved);
    }

    private BigDecimal calculateShipping(BigDecimal subtotal, int totalWeightGrams) {
        if (subtotal.compareTo(new BigDecimal("100.00")) > 0) return BigDecimal.ZERO.setScale(2);

        BigDecimal kg = BigDecimal.valueOf(totalWeightGrams)
                .divide(new BigDecimal("1000"), 2, RoundingMode.UP);

        if (kg.compareTo(new BigDecimal("5.00")) <= 0) return new BigDecimal("1.50");
        if (kg.compareTo(new BigDecimal("10.00")) <= 0) return new BigDecimal("10.00");
        return new BigDecimal("15.00");
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getMyOrder(Long orderId, String username) {
        Order o = orderRepository.findByIdAndUser_Username(orderId, username)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return OrderResponse.from(o);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> listMyOrders(Pageable pageable, String username) {
        Page<Order> page = orderRepository.findByUser_UsernameOrderByPlacedAtDesc(username, pageable);
        return page.map(OrderResponse::from);
    }
}
