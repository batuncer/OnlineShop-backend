package com.onlineShop.bootcamp.repository;

import com.onlineShop.bootcamp.entity.Order;
import com.onlineShop.bootcamp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
