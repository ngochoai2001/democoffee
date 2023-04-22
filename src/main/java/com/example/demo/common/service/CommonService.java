package com.example.demo.common.service;

import com.example.demo.common.dto.VoucherReponse;
import com.example.demo.common.model.Header;
import com.example.demo.common.model.UserVoucher;
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
        Users user = SaveAccount.users;
        return commonRepository.getUsersVouchers(user.getId()).stream().map(vch -> copyVoucher(vch))
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
        commonRepository.save(vch);
        return vch;
    }
    public List<Voucher> getVoucher(Voucher vchs){
        return commonRepository.findAll();
    }

}
