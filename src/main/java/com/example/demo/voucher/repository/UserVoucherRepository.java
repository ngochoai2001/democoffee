package com.example.demo.voucher.repository;

import com.example.demo.voucher.model.UserVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, Integer> {
    @Query("select e from UserVoucher e where e.vch.date >= current_date and e.user.id= :user_id and e.isUsed = false")
    public List<UserVoucher> getUsersVouchers(@Param("user_id") long user_id);

    UserVoucher findUserVoucherById(int id);
}
