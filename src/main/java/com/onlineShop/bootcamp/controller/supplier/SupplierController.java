package com.onlineShop.bootcamp.controller.supplier;


import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.supplier.SupplierRequest;
import com.onlineShop.bootcamp.dto.supplier.SupplierResponse;
import com.onlineShop.bootcamp.service.supplier.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(@RequestBody SupplierRequest supplierRequest) {
        SupplierResponse response = supplierService.createSupplier(supplierRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Supplier created",response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getAllSuppliers() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Supplier created",supplierService.getAllSuppliers()));
    }

}
