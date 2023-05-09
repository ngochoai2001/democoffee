package com.example.demo.voucher.controller;

import com.example.demo.voucher.dto.AddResponse;
import com.example.demo.voucher.dto.AddVoucherRequest;
import com.example.demo.voucher.dto.VoucherReponse;
import com.example.demo.voucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Voucher> addVoucher(@ModelAttribute AddVoucherRequest voucher){
        Voucher vch = voucherService.addVoucher(voucher, voucher.getFile());
        return new ResponseEntity<>(vch, HttpStatus.OK);
    }
    @PostMapping(path = "update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Voucher> addVoucher(@PathVariable("id") int id,@ModelAttribute AddVoucherRequest voucher){
        Voucher vch = voucherService.update(id, voucher, voucher.getFile());
        return new ResponseEntity<>(vch, HttpStatus.OK);
    }
}
