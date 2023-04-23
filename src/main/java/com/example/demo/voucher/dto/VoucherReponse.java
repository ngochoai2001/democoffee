package com.example.demo.voucher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoucherReponse {
    int id;
    String image;
    String name;
    String date;
    private long expireDateLeft;
    private boolean isUsed;
    String description;
    String voucherType;
    String voucherDiscountType;
    private float discount;
}
