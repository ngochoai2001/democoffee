package com.example.demo.voucher.dto;

import com.example.demo.voucher.model.VoucherDiscountType;
import com.example.demo.voucher.model.VoucherType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class AddVoucherRequest {
    String name;
    String quantity;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern="dd-MM-yyyy")
    Date date;
    String description;
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;
    @Enumerated(EnumType.STRING)
    private VoucherDiscountType voucherDiscountType;
    private float discount;
    private MultipartFile file;
}
