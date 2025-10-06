package com.onlineShop.bootcamp.service.product;

import com.onlineShop.bootcamp.dto.ProductRequest;
import com.onlineShop.bootcamp.dto.ProductResponse;
import com.onlineShop.bootcamp.entity.Product;
import com.onlineShop.bootcamp.entity.Supplier;
import com.onlineShop.bootcamp.mapper.ProductMapper;
import com.onlineShop.bootcamp.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    @PersistenceContext
    private EntityManager em;

    private final ProductMapper mapper;
    private final ProductRepository repository;

    public ProductResponse create(ProductRequest req) {
        if (req.getSupplierId() != null) {
            Supplier ref = em.getReference(Supplier.class, req.getSupplierId());
            Product p = mapper.toEntity(req);
            p.setSupplier(ref);
            em.persist(p);
            log.debug("Created product id={}", p.getId());
            return mapper.toResponse(p);
        }
        Product p = mapper.toEntity(req);
        em.persist(p);
        log.debug("Created product id={}", p.getId());
        return mapper.toResponse(p);
    }

    public ProductResponse get(Long id) {
        Product p = em.find(Product.class, id);
        return p == null ? null : mapper.toResponse(p);
    }

    public List<ProductResponse> list() {
        List<Product> products = em.createQuery("from Product", Product.class).getResultList();
        return products.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public ProductResponse update(Long id, ProductRequest req) {
        Product p = em.find(Product.class, id);
        if (p == null) return null;

        mapper.updateEntity(p, req);

        if (req.getSupplierId() != null) {
            p.setSupplier(em.getReference(Supplier.class, req.getSupplierId()));
        } else {
            p.setSupplier(null);
        }

        log.debug("Updated product id={}", p.getId());
        return mapper.toResponse(p);
    }

    public boolean delete(Long id) {
        Product p = em.find(Product.class, id);
        if (p == null) return false;
        em.remove(p);
        log.debug("Deleted product id={}", id);
        return true;
    }

    @Override
    public Page<ProductResponse> listWithPagination(Pageable pageable, String search) {
        Page<Product> products;
        if (search != null && !search.trim().isEmpty()) {
            products = repository.findByProductNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                    search, search, pageable);
        } else {
            products = repository.findAll(pageable);
        }
        return products.map(mapper::toResponse);
    }

    @Override
    public Page<ProductResponse> search(String query, Pageable pageable) {
        Page<Product> products = repository.findByProductNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(
                query, query, pageable);
        return products.map(mapper::toResponse);
    }
}
