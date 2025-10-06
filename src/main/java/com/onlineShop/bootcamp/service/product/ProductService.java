package com.onlineShop.bootcamp.service.product;

import com.onlineShop.bootcamp.dto.ProductRequest;
import com.onlineShop.bootcamp.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse create(ProductRequest req);

    ProductResponse get(Long id);

    ProductResponse update(Long id, ProductRequest req);

    boolean delete(Long id);

    Page<ProductResponse> search(String query, Pageable pageable);

    Page<ProductResponse> listWithPagination(Pageable pageable, String search);

}
