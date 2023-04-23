package com.example.demo.address;

import org.springframework.stereotype.Service;

import java.util.List;

public interface AddressService {

    Address addAddress(AddressDto addressDto, Long userid);

    Address updateAddress (AddressDto addressDto, Long userid);


}
