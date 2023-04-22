package com.example.demo.common.repository;

import com.example.demo.common.model.Header;
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
    @Query("select e from Voucher e where e.date >= current_date ")
    public List<Voucher> getUsersVouchers();
}
