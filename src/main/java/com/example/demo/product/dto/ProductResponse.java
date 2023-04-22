package com.example.demo.product.dto;

import com.example.demo.product.model.ProductCategory;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductResponse {
    private UUID id;
    String name;
    double cost;
    String description;
    private String image;
    private ProductCategory productCategory;
}
