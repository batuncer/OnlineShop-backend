package com.onlineShop.bootcamp.controller.order;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.CreateOrderRequest;
import com.onlineShop.bootcamp.dto.OrderResponse;
import com.onlineShop.bootcamp.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@RequestBody CreateOrderRequest req) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderResponse res = orderService.placeOrder(username, req);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order placed", res));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getMyOrder(@PathVariable Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderResponse res = orderService.getMyOrder(id, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order fetched successfully", res));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> listMyOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "placedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<OrderResponse> res = orderService.listMyOrders(pageable, username);
        return ResponseEntity.ok(new ApiResponse<>(true, "Orders fetched successfully", res));
    }

}
