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
import com.example.demo.voucher.repository.UserVoucherRepository;
import com.example.demo.voucher.service.VoucherService;
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

    @Autowired
    UserVoucherRepository userVoucherRepository;

    @Autowired
    VoucherService voucherService;

    public static final String PAYPAL_SUCCESS_URL = "pay/success";
    public static final String PAYPAL_CANCEL_URL = "pay/cancel";

    @PostMapping("order_pay_by_paypal")
    public ResponseEntity<?> order_by_paypal(@RequestBody OrderDto orderDto, @RequestParam Long user_id) {
        String token = "";
        try {
            Set<OrderItemDto> setOrderItem = orderDto.getOrder_items();
            for (OrderItemDto i : setOrderItem) {

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(productRepository.findById(i.getProduct_id()));
                orderItem.setSize(i.getSize());
                orderItem.setQuantity(i.getQuantity());
                orderItem.setTopping(i.getTopping());
                orderItemRepository.save(orderItem);
            }

            if (userVoucherRepository.findUserVoucherById(orderDto.getUser_voucher_id()) == null &&
                    orderDto.getUser_voucher_id() != 0)
                return Response.response(null, 400, "Not found user voucher");

            Order order = orderService.createOrder(orderDto, user_id);

            save_data_after_order(orderDto, order);

            order.setPaymentMethod("Paypal");
            orderRepository.save(order);
            Payment payment = payWithPaypalService.pay(order.getTotal() / 23447,
                    "USD", "paypal", "sale",
                    "http://localhost:8080/order/" + PAYPAL_CANCEL_URL,
                    "http://localhost:8080/order/" + PAYPAL_SUCCESS_URL);
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
        } catch (PayPalRESTException e) {
            System.out.println(e);
        }
        return Response.response(null, 400, "Payment pending");
    }

    private void save_data_after_order(@RequestBody OrderDto orderDto, Order order) {
        if (orderDto.getUser_voucher_id() != 0)
            voucherService.changeStatusVoucher(orderDto.getUser_voucher_id(), true);

        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByOrder(order.getId());
        for (OrderItem i : orderItems) {
            i.setOrder(orderRepository.findOrderById(order.getId()));
            orderItemRepository.save(i);
        }
    }

    @PostMapping("order_pay_by_cash")
    public ResponseEntity<?> order_by_cash(@RequestBody OrderDto orderDto, @RequestParam Long user_id) {
        Set<OrderItemDto> setOrderItem = orderDto.getOrder_items();

        if (userVoucherRepository.findUserVoucherById(orderDto.getUser_voucher_id()) == null &&
                orderDto.getUser_voucher_id() != 0)
            return Response.response(null, 400, "Not found user voucher");

        for (OrderItemDto i : setOrderItem) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(productRepository.findById(i.getProduct_id()));
            orderItem.setSize(i.getSize());
            orderItem.setTopping(i.getTopping());
            orderItem.setQuantity(i.getQuantity());
            orderItemRepository.save(orderItem);
        }

        Order order = orderService.createOrder(orderDto, user_id);
        order.setPaymentMethod("Cash");
        save_data_after_order(orderDto, order);
        orderRepository.save(order);

        return Response.response(order, 200, "Order success");
    }

    @GetMapping(value = PAYPAL_CANCEL_URL)
    public ResponseEntity<?> cancelPay(@RequestParam("token") String token) {
        PaymentInfo paymentInfo = paymentRepository.findPaymentInfoByToken(token);
        Order order = paymentInfo.getOrder();
        paymentInfo.setStatus("UNPAID");
        order.setStatus("CANCEL");
        paymentRepository.save(paymentInfo);
        orderRepository.save(order);
        if (order.getVoucher() != null)
            voucherService.changeStatusVoucher(order.getVoucher().getId(), false);
        return Response.response(null, 200, "Cancel Payment Success");
    }

    @GetMapping(value = PAYPAL_SUCCESS_URL)
    public ResponseEntity<?> successPay(@RequestParam("token") String token,
                                        @RequestParam("paymentId") String payment_id,
                                        @RequestParam("PayerID") String payer_id) {
        try {
            Payment payment = payWithPaypalService.executePayment(payment_id, payer_id);
            if (payment.getState().equals("approved")) {
                PaymentInfo paymentInfo = paymentRepository.findPaymentInfoByToken(token);
                Order order = paymentInfo.getOrder();
                paymentInfo.setStatus("PAID");
                order.setStatus("DONE");
                paymentRepository.save(paymentInfo);
                orderRepository.save(order);
                return Response.response(null, 200, "Pay success");
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return Response.response(null, 400, "Pay fail");
    }

    @PutMapping("cancel")
    public ResponseEntity<?> cancelOrder(@RequestParam Long id) {
        Order order = orderRepository.findOrderById(id);
        if (order == null)
            return Response.response(null, 400, "Not found order");
        order.setStatus("CANCEL");
        orderRepository.save(order);
        if (order.getVoucher() != null)
            voucherService.changeStatusVoucher(order.getVoucher().getId(), false);
        return Response.response(null, 200, "Cancel success");
    }


    //admin
    @PutMapping("confirm_order_success")
    public ResponseEntity<?> confirmOrder(@RequestParam Long id) {
        Order order = orderRepository.findOrderById(id);
        if (order == null)
            return Response.response(null, 400, "Not found order");
        order.setStatus("DONE");
        orderRepository.save(order);
        return Response.response(null, 200, "Confirm order success");
    }


    @GetMapping("user/get_list_order")
    public ResponseEntity<?> getOrderByUser(@RequestParam Long user_id) {
        return Response.response(orderRepository.getOrderByUser(user_id), 200, "Success");
    }

    @PutMapping("rating_order")
    public ResponseEntity<?> ratingOrder(@RequestParam Long id, @RequestBody int rate ){
        Order order =  orderRepository.findOrderById(id);
        if (order==null)
            return Response.response(null, 400, "Not found order");
        order.setRating(rate);
        return Response.response(orderRepository.save(order), 200, "Success");
    }

}
