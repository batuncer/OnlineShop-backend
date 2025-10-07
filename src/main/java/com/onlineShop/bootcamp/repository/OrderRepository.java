package com.onlineShop.bootcamp.repository;
import com.onlineShop.bootcamp.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>{

    Page<Order> findByUser_IdOrderByPlacedAtDesc(Long userId, Pageable pageable);
    Page<Order> findByUser_UsernameOrderByPlacedAtDesc(String username, Pageable pageable);
    @EntityGraph(attributePaths = "items")
    Order findWithItemsById(Long id);
    @Query("""
           select coalesce(sum(o.total), 0)
           from Order o
           where o.placedAt between :from and :to
           """)
    java.math.BigDecimal sumRevenueBetween(OffsetDateTime from, OffsetDateTime to);

    long countByUser_Id(Long userId);

    Order findTopByUser_IdOrderByPlacedAtDesc(Long userId);
    List<Order> findByPlacedAtBetweenOrderByPlacedAtDesc(OffsetDateTime from, OffsetDateTime to);

}
