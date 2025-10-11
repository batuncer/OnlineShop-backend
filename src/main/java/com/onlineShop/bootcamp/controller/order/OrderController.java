package com.onlineShop.bootcamp.controller.order;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.order.OrderPreviewResponse;
import com.onlineShop.bootcamp.dto.order.OrderRequest;
import com.onlineShop.bootcamp.dto.order.OrderResponse;
import com.onlineShop.bootcamp.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/preview")
    public ResponseEntity<ApiResponse<OrderPreviewResponse>> previewOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order preview", orderService.previewOrder(orderRequest)));
    }

    //Admin role
    @PreAuthorize("hasRole('ADMIN")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order list are fetched", orderService.getAllOrders()));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order is created", orderService.createOrder(orderRequest)));
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUserOrders() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order list are fetched for the user", orderService.getUserOrders()));
    }

}
