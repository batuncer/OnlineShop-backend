package com.onlineShop.bootcamp.service.product;

import com.onlineShop.bootcamp.dto.product.ProductResponse;
import com.onlineShop.bootcamp.entity.Product;
import com.onlineShop.bootcamp.entity.Supplier;
import com.onlineShop.bootcamp.mapper.ProductMapper;
import com.onlineShop.bootcamp.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    private ProductMapper productMapper;

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
        productService = new ProductServiceImpl(productMapper, productRepository);
    }

    @Test
    void listWithPaginationUsesFindAllWhenSearchBlank() {
        Pageable pageable = PageRequest.of(0, 5);
        Supplier supplier = Supplier.builder().id(5L).name("Acme").build();
        Product product = Product.builder()
                .id(10L)
                .supplier(supplier)
                .productName("Breakfast Tea")
                .amountGrams(250)
                .priceGbp(4.5)
                .category("TEA")
                .stockQuantity(8)
                .description("Classic breakfast blend")
                .build();
        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductResponse> responsePage = productService.listWithPagination(pageable, "   ");

        assertThat(responsePage.getTotalElements()).isEqualTo(1);
        ProductResponse response = responsePage.getContent().get(0);
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getSupplierId()).isEqualTo(5L);
        assertThat(response.getTypeName()).isEqualTo("Breakfast Tea");
        assertThat(response.getCategory()).isEqualTo("TEA");
        assertThat(response.getStockQuantity()).isEqualTo(8);

        verify(productRepository).findAll(pageable);
        verify(productRepository, never()).findByProductNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void listWithPaginationUsesSearchWhenQueryProvided() {
        Pageable pageable = PageRequest.of(1, 3);
        Product espresso = Product.builder()
                .id(20L)
                .productName("Espresso Roast")
                .amountGrams(500)
                .priceGbp(7.25)
                .category("COFFEE")
                .build();
        Page<Product> productPage = new PageImpl<>(List.of(espresso), pageable, 1);

        when(productRepository.findByProductNameContainingIgnoreCaseOrCategoryContainingIgnoreCase("espresso", "espresso", pageable))
                .thenReturn(productPage);

        Page<ProductResponse> responsePage = productService.listWithPagination(pageable, "espresso");

        assertThat(responsePage.getContent()).hasSize(1);
        assertThat(responsePage.getContent().get(0).getTypeName()).isEqualTo("Espresso Roast");

        verify(productRepository).findByProductNameContainingIgnoreCaseOrCategoryContainingIgnoreCase("espresso", "espresso", pageable);
        verify(productRepository, never()).findAll(any(Pageable.class));
    }
}
