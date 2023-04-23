package com.example.demo.voucher.controller;

import com.example.demo.voucher.dto.AddResponse;
import com.example.demo.voucher.dto.VoucherReponse;
import com.example.demo.voucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.demo.voucher.model.Voucher;

@RestController
@RequestMapping("vouchers")
public class VoucherController {

    @Autowired
    VoucherService voucherService;
    @GetMapping("")
    public List<VoucherReponse> getUsersVouchers(){
        return voucherService.getUsersVouchers();
    }
    @PostMapping("add")
    public AddResponse addVoucher(@RequestBody Voucher voucher){
        voucherService.addVoucher(voucher);
        return new AddResponse("Thêm voucher thành công", 200);
    }
}
