package com.onlineShop.bootcamp.controller.product;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.ProductRequest;
import com.onlineShop.bootcamp.dto.ProductResponse;
import com.onlineShop.bootcamp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.net.URI;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    //Admin role
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@RequestBody ProductRequest req) {
        ProductResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/products/" + created.getId()))
                .body(new ApiResponse<>(true, "Product created successfully", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> get(@PathVariable Long id) {
        ProductResponse res = service.get(id);
        return res == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(new ApiResponse<>(true, "Product fetched successfully", res));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> list() {
        List<ProductResponse> products = service.list();
        return ResponseEntity.ok(new ApiResponse<>(true, "All products fetched", products));
    }

    //Admin role
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(@PathVariable Long id, @RequestBody ProductRequest req) {
        ProductResponse res = service.update(id, req);
        return res == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(new ApiResponse<>(true, "Product updated successfully", res));
    }

    //Admin role
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.ok(new ApiResponse<>(true, "Product is deleted", null)) : ResponseEntity.notFound().build();
    }
}