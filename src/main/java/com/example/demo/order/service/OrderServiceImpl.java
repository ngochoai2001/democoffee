package com.example.demo.order.service;

import com.example.demo.address.AddressRepository;
import com.example.demo.order.dto.OrderDto;
import com.example.demo.order.dto.OrderItemDto;
import com.example.demo.order.model.Order;
import com.example.demo.order.model.OrderItem;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.voucher.repository.UserVoucherRepository;
import com.example.demo.voucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserVoucherRepository userVoucherRepository;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Order createOrder(OrderDto orderDto, Long user_id) {
        Order order = new Order();
        order.setUser(userRepository.findUsersById(user_id));
        order.setTotal(orderDto.getTotal());
        order.setAddress(addressRepository.findAddressById(orderDto.getAddress_id()));
        order.setReceiver_name(orderDto.getReceiver_name());
        order.setReceiver_phone(orderDto.getReceiver_phone());
//        order.setOrder_items(orderDto.getOrder_items());
        Set<OrderItem> orderItems = new HashSet<>();
        Set<OrderItemDto> orderItemDtos = orderDto.getOrder_items();
        for(OrderItemDto i : orderItemDtos){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(productRepository.findById(i.getProduct_id()));
            orderItem.setSize(i.getSize());
            orderItem.setQuantity(i.getQuantity());
            orderItem.setTopping(i.getTopping());
            orderItems.add(orderItem);
        }
        order.setStatus("Pending");
        order.setVoucher(userVoucherRepository.findUserVoucherById(orderDto.getUser_voucher_id()));
        return orderRepository.save(order);
    }

}
