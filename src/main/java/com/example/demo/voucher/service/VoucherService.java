package com.example.demo.voucher.service;

import com.example.demo.product.awss3.BucketName;
import com.example.demo.product.model.Product;
import com.example.demo.product.service.FileStore;
import com.example.demo.user.model.SaveAccount;
import com.example.demo.user.model.Users;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.utils.DateUtils;
import com.example.demo.utils.ImageUtils;
import com.example.demo.voucher.dto.AddVoucherRequest;
import com.example.demo.voucher.dto.VoucherReponse;
import com.example.demo.voucher.model.UserVoucher;
import com.example.demo.voucher.model.Voucher;
import com.example.demo.voucher.repository.UserVoucherRepository;
import com.example.demo.voucher.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    UserVoucherRepository userVoucherRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    FileStore fileStore;


    public List<VoucherReponse> getUsersVouchers() {
        Users user = SaveAccount.users;
        return userVoucherRepository.getUsersVouchers(user.getId()).stream().map(vch -> copyVoucher(vch))
                .collect(Collectors.toList())
                ;
    }

    public VoucherReponse copyVoucher(UserVoucher vch) {
        VoucherReponse response = new VoucherReponse(
                vch.getId(), vch.getVch().getImage(), vch.getVch().getName(),
                DateUtils.dateFormat(vch.getVch().getDate()), vch.getVch().getExpiredDaysLeft(), vch.isUsed(), vch.getVch().getDescription(),
                vch.getVch().getVoucherType().name(), vch.getVch().getVoucherDiscountType().name(), vch.getVch().getDiscount()
        );
        return response;
    }

    public Voucher addVoucher(AddVoucherRequest request, MultipartFile file) {
        Voucher vch = new Voucher();
        voucherSetValue(vch, request, file);
        List<Users> all = userRepository.findAll();
        voucherRepository.save(vch);
        all.stream().forEach(user ->{
            UserVoucher userVoucher = new UserVoucher();
            userVoucher.setUsed(false);
            userVoucher.setUser(user);
            userVoucher.setVch(vch);
            userVoucherRepository.save(userVoucher);
        });
        return vch;
    }

    public List<Voucher> getVoucher(Voucher vchs) {
        return voucherRepository.findAll();
    }

    public void changeStatusVoucher(int id, boolean status) {
        UserVoucher userVoucher = userVoucherRepository.findUserVoucherById(id);
        userVoucher.setUsed(status);
        userVoucherRepository.save(userVoucher);
    }

    public Voucher update(int id, AddVoucherRequest voucher, MultipartFile file) {
        Voucher vch = voucherRepository.findById(id).get();
        voucherSetValue(vch, voucher, file);
        voucherRepository.save(vch);

        return vch;

    }

    private void voucherSetValue(Voucher vch, AddVoucherRequest request, MultipartFile file) {
        ImageUtils.isFileEmpty(file);
        ImageUtils.isImage(file);
        Map<String, String> metadata = ImageUtils.extractMetadata(file);
        vch.setDiscount(request.getDiscount());
        vch.setDate(request.getDate());
        vch.setName(request.getName());
        vch.setQuantity(request.getQuantity());
        vch.setVoucherType(request.getVoucherType());
        vch.setDescription(request.getDescription());
        vch.setVoucherDiscountType(request.getVoucherDiscountType());
        String path = String.format("%s/voucher", BucketName.PRODUCT_IMAGE.getBucketName());
        String filename = String.format("%s", file.getOriginalFilename());
//
        vch.setImage(Product.ORIGINAL_PATH + "voucher/" + filename);
        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
        } catch (
                IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
