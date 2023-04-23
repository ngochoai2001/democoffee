package com.example.demo.order.service;

import com.example.demo.address.AddressRepository;
import com.example.demo.order.dto.OrderDto;
import com.example.demo.order.model.Order;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.voucher.repository.UserVoucherRepository;
import com.example.demo.voucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Order createOrder(OrderDto orderDto, Long user_id) {
        Order order = new Order();
        order.setUser(userRepository.findUsersById(user_id));
        order.setTotal(orderDto.getTotal());
        order.setAddress(addressRepository.findAddressById(orderDto.getAddress_id()));
        order.setOrder_items(orderDto.getOrder_items());
        order.setPaymentMethod("Paypal");
        order.setVoucher(userVoucherRepository.findUserVoucherById(orderDto.getUser_voucher_id()));
        return orderRepository.save(order);
    }

}
