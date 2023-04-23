package com.example.demo.voucher.service;

import com.example.demo.user.model.SaveAccount;
import com.example.demo.user.model.Users;
import com.example.demo.utils.DateUtils;
import com.example.demo.voucher.dto.VoucherReponse;
import com.example.demo.voucher.model.UserVoucher;
import com.example.demo.voucher.model.Voucher;
import com.example.demo.voucher.repository.UserVoucherRepository;
import com.example.demo.voucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    UserVoucherRepository userVoucherRepository;

    public List<VoucherReponse> getUsersVouchers(){
        Users user = SaveAccount.users;
        return userVoucherRepository.getUsersVouchers(user.getId()).stream().map(vch -> copyVoucher(vch))
                .collect(Collectors.toList())
                ;
    }
    public VoucherReponse copyVoucher(UserVoucher vch){
        VoucherReponse response = new VoucherReponse(
                vch.getId(), vch.getVch().getImage(), vch.getVch().getName(),
                DateUtils.dateFormat(vch.getVch().getDate()),vch.getVch().getExpiredDaysLeft(),vch.isUsed(),vch.getVch().getDescription()
        );
        return response;
    }
    public Voucher addVoucher(Voucher vch){
        voucherRepository.save(vch);
        return vch;
    }
    public List<Voucher> getVoucher(Voucher vchs){
        return voucherRepository.findAll();
    }

}
