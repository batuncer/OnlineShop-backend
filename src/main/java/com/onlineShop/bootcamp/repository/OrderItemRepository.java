package com.onlineShop.bootcamp.repository;

import com.onlineShop.bootcamp.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface  OrderItemRepository  extends JpaRepository<OrderItem, Long>{
    List<OrderItem> findByOrder_Id(Long orderId);

    long deleteByOrder_Id(Long orderId);

    @Query("""
               select i.productId as productId, sum(i.quantity) as totalQty
               from OrderItem i
               group by i.productId
               order by totalQty desc
               """)

    List<Object[]> findTopProductsByQuantity();
}
