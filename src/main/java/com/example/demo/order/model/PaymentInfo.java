package com.example.demo.order.model;

import com.example.demo.user.model.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class PaymentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double total;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime date;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String currency;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    private String token;

    private String status;

}
