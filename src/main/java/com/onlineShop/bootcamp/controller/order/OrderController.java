package com.onlineShop.bootcamp.controller.order;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.order.OrderPreviewResponse;
import com.onlineShop.bootcamp.dto.order.OrderRequest;
import com.onlineShop.bootcamp.dto.order.OrderResponse;
import com.onlineShop.bootcamp.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints for previewing, creating, and managing orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Preview order totals",
            description = "Calculates the total and shipping costs for an order without creating it."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Order preview returned successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid order items supplied",
                    content = @Content
            )
    })
    @PostMapping("/preview")
    public ResponseEntity<ApiResponse<OrderPreviewResponse>> previewOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order preview", orderService.previewOrder(orderRequest)));
    }

    //Admin role
    @PreAuthorize("hasRole('ADMIN")
    @Operation(
            summary = "Fetch all orders",
            description = "Returns every order in the system. Restricted to administrators.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Orders fetched successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order list are fetched", orderService.getAllOrders()));
    }

    @Operation(
            summary = "Create an order",
            description = "Creates a new order for the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Order created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order is created", orderService.createOrder(orderRequest)));
    }

    @Operation(
            summary = "List current user orders",
            description = "Returns the authenticated user's order history.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Orders fetched successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            )
    })
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUserOrders() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Order list are fetched for the user", orderService.getUserOrders()));
    }

    @Operation(
            summary = "Delete an order",
            description = "Deletes a specific order by identifier for the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Order deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Order not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<String>> deleteOrder(
            @Parameter(description = "Identifier of the order to delete", required = true)
            @PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Order is deleted", null));
    }

}
