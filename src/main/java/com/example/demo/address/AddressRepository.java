package com.example.demo.address;

import com.example.demo.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query(value = "select * from address where user_id= :id", nativeQuery = true)
    List<Address> getAddressByUserId(@Param("id") Long id);

    Address findAddressById(Long id);
}
