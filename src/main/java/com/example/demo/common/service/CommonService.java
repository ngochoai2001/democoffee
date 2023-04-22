package com.example.demo.common.service;

import com.example.demo.common.dto.VoucherReponse;
import com.example.demo.common.model.Header;
import com.example.demo.common.model.Voucher;
import com.example.demo.common.repository.CommonRepository;
import com.example.demo.user.model.SaveAccount;
import com.example.demo.user.model.Users;
import com.example.demo.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommonService {
    @Autowired
    private CommonRepository commonRepository;

    public List<Header> getHeaders(){
        return commonRepository.getHeaders();
    }
    public List<VoucherReponse> getUsersVouchers(){


        return commonRepository.getUsersVouchers().stream().map(voucher -> copyVoucher(voucher))
                .collect(Collectors.toList())
                ;
    }
    public VoucherReponse copyVoucher(Voucher voucher){
        VoucherReponse response = new VoucherReponse(
                voucher.getId(), voucher.getImage(), voucher.getName(), voucher.getQuantity()+"",
            DateUtils.dateFormat(voucher.getDate()),voucher.getExpiredDaysLeft(),voucher.getDescription()
        );
        return response;
    }
    public Voucher addVoucher(Voucher voucher){
        commonRepository.save(voucher);
        return voucher;
    }

}
