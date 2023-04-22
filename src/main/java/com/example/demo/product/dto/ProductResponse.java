package com.example.demo.product.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;
    String name;
    double cost;
    String description;
    private byte[] image;
}
