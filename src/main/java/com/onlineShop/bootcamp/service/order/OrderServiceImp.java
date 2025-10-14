package com.onlineShop.bootcamp.service.order;

import com.onlineShop.bootcamp.dto.order.*;
import com.onlineShop.bootcamp.entity.Order;
import com.onlineShop.bootcamp.entity.OrderItem;
import com.onlineShop.bootcamp.entity.Product;
import com.onlineShop.bootcamp.entity.User;
import com.onlineShop.bootcamp.mapper.OrderMapper;
import com.onlineShop.bootcamp.repository.OrderRepository;
import com.onlineShop.bootcamp.repository.ProductRepository;
import com.onlineShop.bootcamp.repository.UserRepository;
import com.onlineShop.bootcamp.security.JwtUtil;
import com.onlineShop.bootcamp.service.shippingCost.ShippingCostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ShippingCostService shippingCostService;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);

    @Transactional
    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new RuntimeException("Missing header");
        }

        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with " + userId));

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;
        double totalWeight = 0.0;

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .orderItemList(orderItems)
                .build();

        // Loop for each requested item
        for(OrderItemRequest item : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(() -> new RuntimeException("Product not found" + item.getProductId()));

            if(product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for this item");
            }

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            totalPrice += product.getPriceGbp() * item.getQuantity();
            totalWeight += product.getAmountGrams() * item.getQuantity();


            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(product.getPriceGbp())
                    .order(order)
                    .build();
           order.getOrderItemList().add(orderItem);

        }

        double shippingCost = shippingCostService.calculateShippingCost(totalPrice,totalWeight );

        order.setTotalPrice(totalPrice+shippingCost);
        order.setShippingCost(shippingCost);

        Order savedOrder = orderRepository.save(order);

        orderRepository.save(savedOrder);
        logger.info("Order created with id {} for user id: {}", savedOrder.getId(), userId);

        return OrderMapper.toOrderResponse(savedOrder);
    }


    @Override
    public List<OrderResponse> getUserOrders() {


        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new RuntimeException("Missing header");
        }

        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with " + userId));

        List<Order> orders = orderRepository.findByUser(user);

        logger.info("Fetching orders for user id {}", userId);

        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getAllOrders() {

        List<Order> orders = orderRepository.findAll();
        logger.info("Fetching all orders for admin");
        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());

    }



    @Override
    public OrderPreviewResponse previewOrder(OrderRequest orderRequest) {
        double totalPrice = 0.0;
        double totalWeight = 0.0;
        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItemRequest orderItem : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(orderItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with " + orderItem.getProductId()));

            double itemTotal = product.getPriceGbp().doubleValue() * orderItem.getQuantity();
            totalPrice += itemTotal;
            totalWeight += product.getAmountGrams() * orderItem.getQuantity();

            OrderItemResponse itemResponse = new OrderItemResponse(
                    product.getId(),
                    product.getProductName(),
                    orderItem.getQuantity(),
                    product.getPriceGbp(),
                    itemTotal
            );

            itemResponses.add(itemResponse);
        }

        double shippingCost = shippingCostService.calculateShippingCost(totalPrice, totalWeight);

        return new OrderPreviewResponse(totalPrice, shippingCost, itemResponses);
    }

    @Transactional
    @Override
    public void deleteOrder(Long orderId) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing header");
        }

        String token = authHeader.substring(7);
        Long userId = jwtUtil.extractUserId(token);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id " + orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this order");
        }

        orderRepository.delete(order);
        logger.info("Order deleted with id {} for user id: {}", orderId, userId);
    }

}
