package com.example.demo.order.repository;

import com.example.demo.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOrderById(Long id);

    @Query(value = "select * from orders where user_id= :id", nativeQuery = true)
    List<Order> getOrderByUser(@Param("id") Long id);

    Order getOrderById(Long id);



}
