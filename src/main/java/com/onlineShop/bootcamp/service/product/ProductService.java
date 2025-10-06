package com.onlineShop.bootcamp.service.product;

import com.onlineShop.bootcamp.dto.ProductRequest;
import com.onlineShop.bootcamp.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest req);

    ProductResponse get(Long id);

    List<ProductResponse> list();

    ProductResponse update(Long id, ProductRequest req);

    boolean delete(Long id);
}
