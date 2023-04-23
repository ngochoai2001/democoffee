package com.example.demo.order.repository;

import com.example.demo.order.model.Order;
import com.example.demo.order.model.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentInfo, Long> {
    PaymentInfo findPaymentInfoByToken(String token);
}
