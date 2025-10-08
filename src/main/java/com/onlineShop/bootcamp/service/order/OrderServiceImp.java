package com.onlineShop.bootcamp.service.order;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.order.OrderItemRequest;
import com.onlineShop.bootcamp.dto.order.OrderRequest;
import com.onlineShop.bootcamp.dto.order.OrderResponse;
import com.onlineShop.bootcamp.entity.Order;
import com.onlineShop.bootcamp.entity.OrderItem;
import com.onlineShop.bootcamp.entity.Product;
import com.onlineShop.bootcamp.entity.User;
import com.onlineShop.bootcamp.mapper.OrderMapper;
import com.onlineShop.bootcamp.repository.OrderRepository;
import com.onlineShop.bootcamp.repository.ProductRepository;
import com.onlineShop.bootcamp.repository.UserRepository;
import com.onlineShop.bootcamp.service.shippingCost.ShippingCostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ShippingCostService shippingCostService;

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImp.class);

    @Transactional
    @Override
    public OrderResponse createOrder(Long userId, OrderRequest orderRequest) {
        logger.info("Creating order for userId: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found" + userId));

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;
        double totalWeight = 0.0;
        // Loop for each requested item
        for(OrderItemRequest item : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(item.getProductid()).orElseThrow(() -> new RuntimeException("Product not found" + item.getProductid()));

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(product.getPriceGbp())
                    .build();
            orderItems.add(orderItem);

            totalPrice += Integer.parseInt(String.valueOf(product.getPriceGbp())) * item.getQuantity();
            totalWeight += product.getAmountGrams() * item.getQuantity();

            if(product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for this item");
            }

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

        }

        double shippingCost = shippingCostService.calculateShippingCost(totalPrice,totalWeight );

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalPrice(totalPrice + shippingCost)
                .orderItemList(orderItems)
                .build();

        Order savedOrder = orderRepository.save(order);

        for(OrderItem item : orderItems){
            item.setOrder(savedOrder);
        }

        orderRepository.save(savedOrder);
        logger.info("Order created with id {} for user id: {}", savedOrder.getId(), userId);

        return OrderMapper.toOrderResponse(savedOrder);
    }


    @Override
    public List<OrderResponse> getUserOrders(Long userId) {
        logger.info("Fetching orders for user id {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        List<Order> orders = orderRepository.findByUser(user);

        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        logger.info("Fetching all orders for admin");
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(OrderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Double> previewOrder(OrderRequest orderRequest) {
        double totalPrice = 0.0;
        double totalWeight = 0.0;

        for(OrderItemRequest orderItem : orderRequest.getOrderItems()) {
            Product product =productRepository.findById(orderItem.getProductid()).orElseThrow(()-> new RuntimeException("Product not found with " + orderItem.getProductid()));
            totalPrice += Integer.parseInt(String.valueOf(product.getPriceGbp())) * orderItem.getQuantity();
            totalWeight += product.getAmountGrams() * orderItem.getQuantity();
        }
        double shippingCost = shippingCostService.calculateShippingCost(totalPrice,totalWeight );


        return Map.of(
                "totalPrice", totalPrice,
                "shippingCost", shippingCost
        );

    }


}
