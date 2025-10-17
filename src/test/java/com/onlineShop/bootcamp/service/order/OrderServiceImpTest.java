package com.onlineShop.bootcamp.service.order;

import com.onlineShop.bootcamp.dto.order.OrderItemRequest;
import com.onlineShop.bootcamp.dto.order.OrderPreviewResponse;
import com.onlineShop.bootcamp.dto.order.OrderRequest;
import com.onlineShop.bootcamp.entity.Order;
import com.onlineShop.bootcamp.entity.OrderItem;
import com.onlineShop.bootcamp.entity.Product;
import com.onlineShop.bootcamp.entity.User;
import com.onlineShop.bootcamp.repository.OrderRepository;
import com.onlineShop.bootcamp.repository.ProductRepository;
import com.onlineShop.bootcamp.repository.UserRepository;
import com.onlineShop.bootcamp.security.JwtUtil;
import com.onlineShop.bootcamp.service.email.EmailService;
import com.onlineShop.bootcamp.service.shippingCost.ShippingCostService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImpTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShippingCostService shippingCostService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private OrderServiceImp orderService;

    @Test
    void createOrderThrowsWhenAuthorizationHeaderMissing() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderItems(List.of());

        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> orderService.createOrder(orderRequest));

        verify(orderRepository, never()).save(any(Order.class));
        verify(emailService, never()).sendOrderConfirmation(any(Order.class));
    }

    @Test
    void createOrderPersistsAndSendsEmail() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderItems(List.of(new OrderItemRequest(1L, 2)));

        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.extractUserId("token")).thenReturn(1L);

        User user = User.builder().id(1L).email("customer@example.com").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Product product = Product.builder()
                .id(1L)
                .productName("Chamomile Tea")
                .priceGbp(5.0)
                .amountGrams(100)
                .stockQuantity(10)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(shippingCostService.calculateShippingCost(anyDouble(), anyDouble())).thenReturn(3.0);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(99L);
            return order;
        });

        orderService.createOrder(orderRequest);

        verify(productRepository).save(product);
        verify(orderRepository).save(any(Order.class));
        verify(emailService).sendOrderConfirmation(argThat(order -> order.getId().equals(99L)));
    }

    @Test
    void previewOrderAggregatesTotalsAndDelegatesToShippingService() {
        OrderRequest request = new OrderRequest();
        request.setOrderItems(List.of(
                new OrderItemRequest(1L, 2),
                new OrderItemRequest(2L, 1)
        ));

        Product chamomileTea = Product.builder()
                .id(1L)
                .productName("Chamomile Tea")
                .priceGbp(3.50)
                .amountGrams(100)
                .build();

        Product earlGrey = Product.builder()
                .id(2L)
                .productName("Earl Grey")
                .priceGbp(5.00)
                .amountGrams(250)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(chamomileTea));
        when(productRepository.findById(2L)).thenReturn(Optional.of(earlGrey));
        when(shippingCostService.calculateShippingCost(anyDouble(), anyDouble())).thenReturn(7.25);

        OrderPreviewResponse response = orderService.previewOrder(request);

        double expectedTotalPrice = (3.50 * 2) + (5.00 * 1);
        double expectedTotalWeight = (100 * 2) + (250 * 1);

        assertThat(response.getTotalPrice()).isEqualTo(expectedTotalPrice);
        assertThat(response.getShippingCost()).isEqualTo(7.25);
        assertThat(response.getItems()).hasSize(2);
        assertThat(response.getItems().get(0).getProductId()).isEqualTo(1L);
        assertThat(response.getItems().get(0).getSubTotal()).isEqualTo(7.0);
        assertThat(response.getItems().get(1).getProductId()).isEqualTo(2L);
        assertThat(response.getItems().get(1).getSubTotal()).isEqualTo(5.0);

        verify(shippingCostService).calculateShippingCost(expectedTotalPrice, expectedTotalWeight);
    }

    @Test
    void deleteOrderThrowsWhenAuthorizationHeaderMissing() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> orderService.deleteOrder(42L));
        verify(orderRepository, never()).delete(any(Order.class));
        verify(emailService, never()).sendOrderCancellation(any(Order.class));
    }

    @Test
    void deleteOrderThrowsWhenOrderNotFound() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.extractUserId("token")).thenReturn(99L);
        when(orderRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.deleteOrder(5L));
        verify(orderRepository, never()).delete(any(Order.class));
        verify(emailService, never()).sendOrderCancellation(any(Order.class));
    }

    @Test
    void deleteOrderThrowsWhenUserDoesNotOwnOrder() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.extractUserId("token")).thenReturn(1L);

        User otherUser = User.builder().id(2L).build();
        Order order = Order.builder().id(7L).user(otherUser).build();
        when(orderRepository.findById(7L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> orderService.deleteOrder(7L));
        verify(orderRepository, never()).delete(any(Order.class));
        verify(emailService, never()).sendOrderCancellation(any(Order.class));
    }

    @Test
    void deleteOrderRestoresStockAndRemovesOrderWhenOwnedByUser() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtUtil.extractUserId("token")).thenReturn(1L);

        User owner = User.builder().id(1L).build();
        Product product = Product.builder().id(4L).stockQuantity(5).build();
        OrderItem orderItem = OrderItem.builder()
                .id(11L)
                .order(null)
                .product(product)
                .quantity(2)
                .price(5.0)
                .build();

        Order order = Order.builder()
                .id(10L)
                .user(owner)
                .orderItemList(List.of(orderItem))
                .build();
        orderItem.setOrder(order);
        when(orderRepository.findById(10L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(10L);

        assertThat(product.getStockQuantity()).isEqualTo(7);
        verify(productRepository).save(product);
        verify(emailService).sendOrderCancellation(order);
        verify(orderRepository).delete(order);
    }
}
