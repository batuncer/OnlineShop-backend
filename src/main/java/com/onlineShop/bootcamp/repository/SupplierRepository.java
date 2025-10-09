package com.onlineShop.bootcamp.repository;

import com.onlineShop.bootcamp.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> { }

