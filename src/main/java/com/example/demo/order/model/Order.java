package com.example.demo.order.model;
//

import com.example.demo.address.Address;
import com.example.demo.user.model.Users;
import com.example.demo.voucher.model.UserVoucher;
import com.example.demo.voucher.model.Voucher;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "order")
@Entity
@Data
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
    @OneToMany(mappedBy = "order")
    private Set<OrderItem> order_items;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime time_order;

    private String paymentMethod;

    @OneToOne
    @JoinColumn(name = "voucher_id")
    @JsonBackReference
    private UserVoucher voucher;

    private String status;

    private int rating;


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