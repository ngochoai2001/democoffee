package com.example.demo.order.controller;

import com.example.demo.common.Response;
import com.example.demo.order.dto.OrderDto;
import com.example.demo.order.dto.OrderItemDto;
import com.example.demo.order.model.Order;
import com.example.demo.order.model.OrderItem;
import com.example.demo.order.model.PaymentInfo;
import com.example.demo.order.repository.OrderItemRepository;
import com.example.demo.order.repository.OrderRepository;
import com.example.demo.order.repository.PaymentRepository;
import com.example.demo.order.service.OrderService;
import com.example.demo.order.service.PayWithPaypalService;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.user.repository.UserRepository;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PayWithPaypalService payWithPaypalService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    public static final String PAYPAL_SUCCESS_URL = "pay/success";
    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    @PostMapping("order_pay_by_paypal")
    public ResponseEntity<?> order_by_paypal(@RequestBody OrderDto orderDto, @RequestParam Long user_id) {
        String token = "";
        try {
            Set<OrderItemDto> setOrderItem = orderDto.getOrder_items();
            for( OrderItemDto i : setOrderItem){
                if ( productRepository.findById(i.getProduct_id()) ==null)
                    return Response.response(null, 400, "Not found product" +
                            productRepository.findById(i.getProduct_id()).getName());
                else {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(productRepository.findById(i.getProduct_id()));
                    orderItem.setSize(i.getSize());
                    orderItem.setQuantity(i.getQuantity());
                    orderItem.setTopping(i.getTopping());
                    orderItemRepository.save(orderItem);
                }
            }

            Order order = orderService.createOrder(orderDto, user_id);
            order.setPaymentMethod("Paypal");
            orderRepository.save(order);
            Payment payment = payWithPaypalService.pay(order.getTotal() / 23447,
                    "USD", "paypal", "thanh toan hoa don",
                    "http://localhost:8080/" + PAYPAL_SUCCESS_URL,
                    "http://localhost:8080/" + PAYPAL_CANCEL_URL);
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setOrder(order);
            paymentInfo.setCurrency("VND");
            paymentInfo.setTotal(order.getTotal());
            paymentInfo.setUsers(userRepository.findUsersById(user_id));
            paymentRepository.save(paymentInfo);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    String[] s = link.getHref().split("=");
                    token = s[2];
                    paymentInfo.setToken(token);
                    paymentRepository.save(paymentInfo);
                    return Response.response(link.getHref(), 200, "Success");

                }
            }
        } catch (PayPalRESTException e){
            System.out.println(e);
        }
        return Response.response(null, 400, "Payment pending");
    }

    @PostMapping("order_pay_by_cash")
    public ResponseEntity<?> order_by_cash(@RequestBody OrderDto orderDto, @RequestParam Long user_id ) {
        Set<OrderItemDto> setOrderItem = orderDto.getOrder_items();

        for( OrderItemDto i : setOrderItem){
            if ( productRepository.findById(i.getProduct_id()) ==null)
                return Response.response(null, 400, "Not found product" +
                        productRepository.findById(i.getProduct_id()).getName());
            else {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(productRepository.findById(i.getProduct_id()));
                orderItem.setSize(i.getSize());
                orderItem.setTopping(i.getTopping());
                orderItem.setQuantity(i.getQuantity());
                orderItemRepository.save(orderItem);
            }
        }
        Order order = orderService.createOrder(orderDto, user_id);
        order.setPaymentMethod("Cash");
        orderRepository.save(order);

        List<OrderItem> orderItems = orderItemRepository.findAll();
        for (OrderItem i : orderItems){
            i.setOrder(orderRepository.findOrderById(order.getId()));
            orderItemRepository.save(i);
        }

        return Response.response(order, 400, "Order success");
    }

    @GetMapping(value = PAYPAL_CANCEL_URL)
    public ResponseEntity<?> cancelPay(@RequestParam("token") String token){
        PaymentInfo paymentInfo = paymentRepository.findPaymentInfoByToken(token);
        Order order = paymentInfo.getOrder();
        paymentInfo.setStatus("UNPAID");
        order.setStatus("CANCEL");
        paymentRepository.save(paymentInfo);
        orderRepository.save(order);
        return Response.response(null, 200, "Cancel Payment Success");
    }

    @GetMapping(value = PAYPAL_SUCCESS_URL)
    public ResponseEntity<?> successPay(@RequestParam("token") String token,
                                        @RequestParam("payment_id") String payment_id,
                                        @RequestParam("payer_id") String payer_id){
        try {
            Payment payment = payWithPaypalService.executePayment(payment_id, payer_id);
            if (payment.getState().equals("approved")){
                PaymentInfo paymentInfo = paymentRepository.findPaymentInfoByToken(token);
                Order order = paymentInfo.getOrder();
                paymentInfo.setStatus("PAID");
                order.setStatus("DONE");
                paymentRepository.save(paymentInfo);
                orderRepository.save(order);
                return Response.response(null, 200, "Pay success");
            }
        }catch (PayPalRESTException e){
            System.out.println(e.getMessage());
        }
        return Response.response(null, 400,"Pay fail");
    }

    @PutMapping("cancel")
    public ResponseEntity<?> cancelOrder(@RequestParam Long id){
        Order order = orderRepository.findOrderById(id);
        if(order ==null)
            return Response.response(null, 400, "Not found order");
        order.setStatus("CANCEL");
        orderRepository.save(order);
        return Response.response(null, 200, "Cancel success");
    }


    //admin
    @PutMapping("confirm_order_success")
    public ResponseEntity<?> confirmOrder(@RequestParam Long id){
        Order order = orderRepository.findOrderById(id);
        if(order ==null)
            return Response.response(null, 400, "Not found order");
        order.setStatus("DONE");
        orderRepository.save(order);
        return Response.response(null, 200, "Confirm order success");
    }



}
