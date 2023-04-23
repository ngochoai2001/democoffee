package com.example.demo.order.repository;

import com.example.demo.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findOrderItemById(Long id);

    @Query(value = "select * from order_item where order_id= :id", nativeQuery = true)
    List<OrderItem> findOrderItemsByOrder(@Param("id") Long id);

}
