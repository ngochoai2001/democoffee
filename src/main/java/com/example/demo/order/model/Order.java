package com.example.demo.order.model;
//

import com.example.demo.address.Address;
import com.example.demo.product.model.Product;
import com.example.demo.user.model.Users;
import com.example.demo.voucher.model.Voucher;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "order")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double total;

    @JoinColumn(name = "address_id")
    @OneToOne
    @JsonBackReference
    private Address address;

    @JoinColumn(name = "user_id")
    @OneToOne
    @JsonBackReference
    private Users user;

    @JsonManagedReference
    @OneToMany
    @JoinColumn(name = "product_order_id")
    private Set<ProductOrder> product_orders;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime time_order;

    //0: tien mat, 1: online
    private int paymentMethod;

    @OneToOne
    @JoinColumn(name = "voucher_id")
    @JsonBackReference
    private Voucher voucher;

    //0 : dang thuc hien, 1: hoan tat, 2 : huy
    private int status;

    private int rating;

    public Order() {
    }

//
//    /* get (đang thực hiện, đã hoàn tất, đã hủy), add, huy don hang, đánh giá đơn hàng
//        - ma don hang
//        - user(ten, so dien thoai)
//        - thoi gian
//        - cac san pham
//        - dia chi
//        - gia tien
//        - phuong thuc thanh toan (tien mat, paypal)
//        - voucher
//        - trang thai don hang (dang thuc hien, da hoan tat, da huy) // 1p đầu đặt hang có quyền hủy đơn hàng,
//                                                                 sau 1p tự động chuyển trạng thái sang đang thực hiện
//        - danh gia don hang (rating bar) = null
//    */
//

}