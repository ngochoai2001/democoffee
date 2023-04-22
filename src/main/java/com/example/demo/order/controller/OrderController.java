package com.example.demo.order.controller;

import com.example.demo.common.Response;
import com.example.demo.order.dto.OrderDto;
import com.example.demo.order.dto.ProductOrderDto;
import com.example.demo.order.model.ProductOrder;
import com.example.demo.order.service.ProductOrderService;
import com.example.demo.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    ProductOrderService productOrderService;

    @Autowired
    ProductRepository productRepository;

    @PostMapping("/create")
    public ResponseEntity<?> order(OrderDto orderDto){

    }

    @PostMapping("/add_product_order")
    public ResponseEntity<?> addProductOrder(ProductOrderDto productOrderDto){
        if(productRepository.findById(productOrderDto.getProduct_id())==null)
            return Response.response(null, 400, "Not found product");

        ProductOrder productOrder = productOrderService.addProductOrder(productOrderDto);
        return Response.response(productOrder, 200, "Success");

    }


}
