package com.example.demo.voucher.model;

import com.example.demo.utils.DateUtils;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String image;
    String name;
    String quantity;
    Date date;
    String description;
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;
    @Enumerated(EnumType.STRING)
    private VoucherDiscountType voucherDiscountType;
    private float discount;

    @Transient
    private long expiredDaysLeft;

    public long getExpiredDaysLeft() {
        Date currentDate  = new Date();
        expiredDaysLeft = DateUtils.getDaysBetween(new Date(), date);
        return expiredDaysLeft;
    }
}