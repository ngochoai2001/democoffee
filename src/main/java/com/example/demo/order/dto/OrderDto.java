package com.example.demo.order.dto;

import com.example.demo.order.model.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class OrderDto {
    private double total;

    private long address_id;

    private int user_voucher_id;

    private Set<OrderItem> order_items;

}
