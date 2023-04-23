package com.example.demo.common.controller;

import com.example.demo.common.dto.AddResponse;
import com.example.demo.common.dto.VoucherReponse;
import com.example.demo.common.model.Header;
import com.example.demo.common.model.Voucher;
import com.example.demo.common.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home")
public class CommonController {
    @Autowired
    private CommonService commonService;
    @GetMapping("/headers")
    public List<Header>  getHeaders(){
        return commonService.getHeaders();
    }
    @GetMapping("/vouchers")
    public List<VoucherReponse> getUsersVouchers(){
        return commonService.getUsersVouchers();
    }
    @PostMapping("/vouchers/add")
    public AddResponse addVoucher(@RequestBody Voucher voucher){
        commonService.addVoucher(voucher);
        return new AddResponse("Thêm voucher thành công", 200);
    }

}
