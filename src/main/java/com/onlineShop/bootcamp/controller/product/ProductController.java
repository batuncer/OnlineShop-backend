package com.onlineShop.bootcamp.controller.product;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.ProductRequest;
import com.onlineShop.bootcamp.dto.ProductResponse;
import com.onlineShop.bootcamp.service.product.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl service;

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
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProductResponse> products = service.listWithPagination(pageable, search);
        return ResponseEntity.ok(new ApiResponse<>(true, "Products fetched successfully", products));
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

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = service.search(query, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Search results", products));
    }
}