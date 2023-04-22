package com.example.demo.common.repository;

import com.example.demo.common.model.Header;
import com.example.demo.common.model.UserVoucher;
import com.example.demo.common.model.Voucher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

@Repository
public interface CommonRepository extends CrudRepository<Voucher, Integer> {
    @Query("select e from Header e")
    public List<Header> getHeaders();
    @Query("select e from UserVoucher e where e.vch.date >= current_date and e.user.id= :user_id")
    public List<UserVoucher> getUsersVouchers(@Param("user_id") long user_id);

    List<Voucher> findAll();
}
