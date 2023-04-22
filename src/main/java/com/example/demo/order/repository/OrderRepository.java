package com.example.demo.order.repository;

import com.example.demo.address.Address;
import com.example.demo.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


}
