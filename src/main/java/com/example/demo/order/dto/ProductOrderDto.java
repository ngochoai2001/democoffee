package com.example.demo.order.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductOrderDto {

    private UUID product_id;
    private String size;
    private String topping;

}
