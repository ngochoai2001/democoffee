package com.example.demo.order.service;

import com.example.demo.order.dto.ProductOrderDto;
import com.example.demo.order.model.ProductOrder;
import com.example.demo.order.repository.ProductOrderRepository;
import com.example.demo.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductOrderRepository productOrderRepository;

    @Override
    public ProductOrder addProductOrder(ProductOrderDto productOrderDto) {
        ProductOrder productOrder = new ProductOrder();
        productOrder.setProduct(productRepository.findById(productOrderDto.getProduct_id()));
        productOrder.setSize(productOrderDto.getSize());
        productOrder.setTopping(productOrderDto.getTopping());
        return  productOrderRepository.save(productOrder);
    }
}
