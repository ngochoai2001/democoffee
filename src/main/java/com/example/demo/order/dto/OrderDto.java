package com.example.demo.order.dto;

import com.example.demo.order.model.OrderItem;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class OrderDto {
    private double total;

    private long address_id;

    private int user_voucher_id;

    private List<OrderItemDto> order_items;

    private String receiver_name;

    private String receiver_phone;

}
