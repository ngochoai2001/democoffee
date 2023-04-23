package com.example.demo.order.dto;

import com.example.demo.product.model.Product;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemDto {

    private UUID product_id;
    private String size;
    private String topping;
    private int quantity;

}
