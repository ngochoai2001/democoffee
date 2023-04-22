package com.example.demo.order.service;

import com.example.demo.order.dto.ProductOrderDto;
import com.example.demo.order.model.ProductOrder;

public interface ProductOrderService {

    ProductOrder addProductOrder(ProductOrderDto productOrderDto);
}
