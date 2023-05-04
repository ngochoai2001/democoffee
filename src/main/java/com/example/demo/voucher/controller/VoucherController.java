package com.example.demo.voucher.controller;

import com.example.demo.voucher.dto.AddResponse;
import com.example.demo.voucher.dto.AddVoucherRequest;
import com.example.demo.voucher.dto.VoucherReponse;
import com.example.demo.voucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.demo.voucher.model.Voucher;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("vouchers")
public class VoucherController {

    @Autowired
    VoucherService voucherService;
    @GetMapping("")
    public List<VoucherReponse> getUsersVouchers(){
        return voucherService.getUsersVouchers();
    }
    @PostMapping(path = "add/",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public AddResponse addVoucher(@ModelAttribute AddVoucherRequest voucher,@RequestParam("file") MultipartFile file){
        voucherService.addVoucher(voucher, file);
        return new AddResponse("Thêm voucher thành công", 200);
    }
    @PostMapping(path = "update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public AddResponse addVoucher(@PathVariable("id") int id,@ModelAttribute AddVoucherRequest voucher,@RequestParam("file") MultipartFile file){
        voucherService.update(id, voucher, file);
        return new AddResponse("Update voucher thành công", 200);
    }
}
