package com.example.demo.order.dto;

import lombok.Data;

@Data
public class OrderDto {
    private double total;

    private long address_id;

    private int paymentMethod;

    private int id;

    private int status;

    private int rating;

}
