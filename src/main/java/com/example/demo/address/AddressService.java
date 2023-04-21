package com.example.demo.address;

import java.util.List;

public interface AddressService {

    Address addAddress(AddressDto addressDto);

    Address updateAddress (AddressDto addressDto, Long id);


}
