package com.onlineShop.bootcamp.controller.supplier;


import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.supplier.SupplierRequest;
import com.onlineShop.bootcamp.dto.supplier.SupplierResponse;
import com.onlineShop.bootcamp.service.supplier.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@Tag(name = "Suppliers", description = "Endpoints for managing suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(
            summary = "Create a supplier",
            description = "Adds a new supplier record.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Supplier created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(@RequestBody SupplierRequest supplierRequest) {
        SupplierResponse response = supplierService.createSupplier(supplierRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Supplier created",response));
    }

    @Operation(
            summary = "List suppliers",
            description = "Retrieves all suppliers available in the system."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Suppliers fetched successfully"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getAllSuppliers() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Supplier created",supplierService.getAllSuppliers()));
    }

}
