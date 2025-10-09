package com.onlineShop.bootcamp.repository;
import com.onlineShop.bootcamp.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>{

    Page<Order> findByUser_UsernameOrderByPlacedAtDesc(String username, Pageable pageable);

    Optional<Order> findByIdAndUser_Username(Long id, String username);

    @EntityGraph(attributePaths = "items")
    Optional<Order> findWithItemsById(Long id);

}
