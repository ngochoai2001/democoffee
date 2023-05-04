package com.example.demo.voucher.dto;

import com.example.demo.voucher.model.VoucherDiscountType;
import com.example.demo.voucher.model.VoucherType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class AddVoucherRequest {
    String name;
    String quantity;
    Date date;
    String description;
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;
    @Enumerated(EnumType.STRING)
    private VoucherDiscountType voucherDiscountType;
    private float discount;
}
