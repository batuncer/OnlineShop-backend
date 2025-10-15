package com.onlineShop.bootcamp.controller.product;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.product.ProductRequest;
import com.onlineShop.bootcamp.dto.product.ProductResponse;
import com.onlineShop.bootcamp.service.product.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Products", description = "Endpoints for managing product catalogue")
public class ProductController {

    private final ProductServiceImpl service;

    //Admin role
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Create a product",
            description = "Adds a new product to the catalogue.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid product details supplied",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@RequestBody ProductRequest req) {
        ProductResponse created = service.create(req);
        return ResponseEntity.created(URI.create("/products/" + created.getId()))
                .body(new ApiResponse<>(true, "Product created successfully", created));
    }

    @Operation(
            summary = "Get product details",
            description = "Retrieves a single product by its identifier."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product fetched successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> get(
            @Parameter(description = "Unique identifier of the product", required = true)
            @PathVariable Long id) {
        ProductResponse res = service.get(id);
        return res == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(new ApiResponse<>(true, "Product fetched successfully", res));
    }

    @Operation(
            summary = "List products",
            description = "Provides a paginated list of products with optional search and sorting."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Products fetched successfully"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> list(
            @Parameter(description = "Zero-based page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "typeName")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Optional text to search for within product fields", example = "chamomile")
            @RequestParam(required = false) String search) {

        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<ProductResponse> products = service.listWithPagination(pageable, search);
        return ResponseEntity.ok(new ApiResponse<>(true, "Products fetched successfully", products));
    }

    //Admin role
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update a product",
            description = "Modifies an existing product.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @Parameter(description = "Unique identifier of the product", required = true)
            @PathVariable Long id,
            @RequestBody ProductRequest req) {
        ProductResponse res = service.update(id, req);
        return res == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(new ApiResponse<>(true, "Product updated successfully", res));
    }

    //Admin role
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete a product",
            description = "Removes a product from the catalogue.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Product deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(
            @Parameter(description = "Unique identifier of the product", required = true)
            @PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.ok(new ApiResponse<>(true, "Product is deleted", null)) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Search products",
            description = "Searches products by keyword with basic pagination."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Search completed successfully"
            )
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> search(
            @Parameter(description = "Keyword to match against product fields", required = true, example = "herbal")
            @RequestParam String query,
            @Parameter(description = "Zero-based page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> products = service.search(query, pageable);
        return ResponseEntity.ok(new ApiResponse<>(true, "Search results", products));
    }
}
