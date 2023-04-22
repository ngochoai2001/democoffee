package com.example.demo.order.repository;

import com.example.demo.address.Address;
import com.example.demo.order.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {


}
